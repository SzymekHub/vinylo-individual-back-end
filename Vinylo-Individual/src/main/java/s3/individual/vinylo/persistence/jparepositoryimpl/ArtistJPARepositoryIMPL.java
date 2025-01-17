package s3.individual.vinylo.persistence.jparepositoryimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.mappers.ArtistEntityMapper;
import s3.individual.vinylo.persistence.ArtistRepo;
import s3.individual.vinylo.persistence.entity.ArtistEntity;
import s3.individual.vinylo.persistence.jparepository.ArtistJPARepo;

@Repository
public class ArtistJPARepositoryIMPL implements ArtistRepo {

    private final ArtistJPARepo artistJPARepo;

    @PersistenceContext
    private EntityManager entityManager;

    public ArtistJPARepositoryIMPL(ArtistJPARepo artistJPARepo) {
        this.artistJPARepo = artistJPARepo;
    }

    @Override
    public Artist saveArtist(Artist artist) {
        ArtistEntity entity = ArtistEntityMapper.toEntity(artist);

        // Check if id is null or 0, then save if it's not then update
        if (entity.getId() == 0) {
            ArtistEntity savArtistEntity = artistJPARepo.save(entity);
            return ArtistEntityMapper.fromEntity(savArtistEntity);
        }

        ArtistEntity mergedArtistEntity = entityManager.merge(entity);

        return ArtistEntityMapper.fromEntity(mergedArtistEntity);
    }

    @Override
    public List<Artist> getArtists(int offset, int limit) {
        String query = "SELECT a FROM ArtistEntity a ORDER BY a.id";

        return entityManager.createQuery(query, ArtistEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList()
                .stream()
                .map(ArtistEntityMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public int getTotalArtistsCount() {
        return artistJPARepo.getTotalArtistsCount();
    }

    @Override
    public Artist getArtistByName(String name) {
        // Look for a VinylEntity in the database by its ID
        return artistJPARepo.findByName(name)
                .map(ArtistEntityMapper::fromEntity)
                // If not found, return null
                .orElse(null);
    }

    @Override
    public Artist getArtistById(int id) {
        // Look for a ArtistEntity in the database by its ID
        return artistJPARepo.findById(id)
                // If found, map the ArtistEntity to a Artist domain object using
                // ArtistEntityMapper
                .map(ArtistEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public boolean deactivateArtistById(int id) {
        try {
            artistJPARepo.deleteByArtistId(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
