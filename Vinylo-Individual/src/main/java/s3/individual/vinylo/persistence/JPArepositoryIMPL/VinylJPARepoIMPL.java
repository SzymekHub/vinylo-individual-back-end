package s3.individual.vinylo.persistence.JPArepositoryIMPL;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.Mappers.VinylEntityMapper;
import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.persistence.JPArepository.VinylJPARepo;

@Repository
public class VinylJPARepoIMPL implements VinylRepo {

    private final VinylJPARepo vinylJPARepo;

    @Autowired
    public VinylJPARepoIMPL(VinylJPARepo vinylJPARepo) {
        this.vinylJPARepo = vinylJPARepo;
    }

    @Override
    public List<Vinyl> getVinyls() {
        return vinylJPARepo.findAll().stream()
                .map(VinylEntityMapper::fromEntity)
                .collect(Collectors.toList());
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
    public Vinyl saveVinyl(Vinyl vinyl) {
        vinylJPARepo.save(VinylEntityMapper.toEntity(vinyl));
        return vinyl;
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
