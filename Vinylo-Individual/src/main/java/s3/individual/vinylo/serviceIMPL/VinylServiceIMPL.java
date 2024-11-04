package s3.individual.vinylo.serviceIMPL;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.VinylRepo;
import s3.individual.vinylo.Models.services.VinylService;
import s3.individual.vinylo.serviceIMPL.domain.Vinyl;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class VinylServiceIMPL implements VinylService {

    private final VinylRepo vinylRepo;

    public VinylServiceIMPL(VinylRepo vinylRepo) {
        this.vinylRepo = vinylRepo;
    }

    // @Transactional -> That’s all it takes to have all queries triggered inside
    // the method inside a same database transaction.
    // !! Always remember of using transactions in your use-cases!
    @Transactional
    @Override
    public Vinyl createNewVinyl(Vinyl newvinyl) {

        return vinylRepo.createNewVinyl(newvinyl);
    }

    @Override
    public Vinyl getVinylById(int id) {
        return vinylRepo.getVinylById(id);
    }

    @Override
    public List<Vinyl> getVinyls() {
        return vinylRepo.getVinyls();
    }

    @Override
    public Vinyl replaceVinyl(int id, Vinyl newVinyl) {
        Vinyl existingVinyl = vinylRepo.getVinylById(id);

        if (existingVinyl != null) {
            existingVinyl.setTitle(newVinyl.getTitle());
            existingVinyl.setDescription(newVinyl.getDescription());
            existingVinyl.setIsReleased(newVinyl.getisReleased());
            existingVinyl.setArtist(newVinyl.getArtist());
            existingVinyl.setVinylType(newVinyl.getvinylType());
            return existingVinyl;
        } else {
            return vinylRepo.createNewVinyl(newVinyl);
        }
    }

    @Override
    public boolean deleteVinylById(int id) {
        return vinylRepo.deleteVinylById(id);
    }
}
