package s3.individual.vinylo.persistence.jparepositoryimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.mappers.VinylEntityMapper;
import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.persistence.entity.ArtistEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.jparepository.VinylJPARepo;

@Repository
public class VinylJPARepoIMPL implements VinylRepo {

    private final VinylJPARepo vinylJPARepo;

    @PersistenceContext
    private EntityManager entityManager;

    public VinylJPARepoIMPL(VinylJPARepo vinylJPARepo) {
        this.vinylJPARepo = vinylJPARepo;
    }

    @Override
    public List<Vinyl> getVinyls(int offset, int limit) {
        String query = "SELECT v FROM VinylEntity v ORDER BY v.id";

        return entityManager.createQuery(query, VinylEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList()
                .stream()
                .map(VinylEntityMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public int getTotalVinylsCount() {
        return vinylJPARepo.getTotalVinylsCount();
    }

    @Override
    public Vinyl getVinylById(int id) {
        // Look for a VinylEntity in the database by its ID
        return vinylJPARepo.findById(id)
                // If found, map the VinylEntity to a Vinyl domain object using
                // VinylEntityMapper
                .map(VinylEntityMapper::fromEntity)
                // If not found, return null
                .orElse(null);
    }

    @Override
    public Vinyl findByArtistAndTitleAndState(int artistId, String title, String state) {
        return vinylJPARepo.findByArtistIdAndTitleAndState(artistId, title, state)
                .map(VinylEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    @Transactional
    public Vinyl saveVinyl(Vinyl vinyl) {
        VinylEntity entity = VinylEntityMapper.toEntity(vinyl);
        ArtistEntity managedArtist = entityManager.merge(entity.getArtist());
        entity.setArtist(managedArtist);
        VinylEntity savedEntity = vinylJPARepo.save(entity);
        return VinylEntityMapper.fromEntity(savedEntity);
    }

    @Override
    public boolean deleteVinylById(int id) {
        if (vinylJPARepo.existsById(id)) {
            vinylJPARepo.deleteById(id);
            return true;
        }
        return false;
    }
}
