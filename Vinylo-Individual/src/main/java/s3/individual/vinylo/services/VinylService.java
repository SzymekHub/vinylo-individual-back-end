package s3.individual.vinylo.services;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.VinylRepo;
import s3.individual.vinylo.services.domain.Vinyl;

import java.util.List;

@Service
public class VinylService {

    private final VinylRepo vinylRepo;

    public VinylService(VinylRepo vinylRepo) {
        this.vinylRepo = vinylRepo;
    }

    public Vinyl createNewVinyl(Vinyl newvinyl) {

        return  vinylRepo.createNewVinyl(newvinyl);
    }

    public Vinyl getVinylById(int id)
    {
        return vinylRepo.getVinylById(id);
    }

    public List<Vinyl> getVinyls()
    {
        return vinylRepo.getVinyls();
    }

    public Vinyl replaceVinyl(int id, Vinyl newVinyl) {
        Vinyl existingVinyl = vinylRepo.getVinylById(id);

        if (existingVinyl != null) {
            existingVinyl.setTitle(newVinyl.getTitle());
            existingVinyl.setDescription(newVinyl.getDescription());
            existingVinyl.setIsReleased(newVinyl.getisReleased());
            existingVinyl.setArtistName(newVinyl.getArtistName());
            existingVinyl.setVinylType(newVinyl.getvinylType());
            return existingVinyl;
        } else {
            return vinylRepo.createNewVinyl(newVinyl);
        }
    }


    public boolean deleteVinylById(int id) {
        return vinylRepo.deleteVinylById(id);
    }
}
