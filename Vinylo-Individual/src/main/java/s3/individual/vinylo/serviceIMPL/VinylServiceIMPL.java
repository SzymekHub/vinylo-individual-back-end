package s3.individual.vinylo.serviceIMPL;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.domain.Vinyl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VinylServiceIMPL implements VinylService {

    private final VinylRepo vinylRepo;

    // @Transactional -> That’s all it takes to have all queries triggered inside
    // the method inside a same database transaction.
    // !! Always remember of using transactions in your use-cases!
    @Override
    @Transactional
    public Vinyl saveVinyl(Integer id, Vinyl newVinyl) {
        // I use Integer here so that it can allow nulls, so i can make new vinyls
        // Integer is a wrapper class for int, which means it can hold an integer value
        // but also allows for null.
        Vinyl existingVinyl = (id != null) ? vinylRepo.getVinylById(id) : null;

        if (existingVinyl != null) {
            // Update the existing vinyl with the new details
            existingVinyl.setTitle(newVinyl.getTitle());
            existingVinyl.setDescription(newVinyl.getDescription());
            existingVinyl.setIsReleased(newVinyl.getisReleased());
            existingVinyl.setArtist(newVinyl.getArtist());
            existingVinyl.setVinylType(newVinyl.getvinylType());

            // Save the updated vinyl to the database
            return vinylRepo.saveVinyl(existingVinyl);
        } else {
            // Create a new vinyl if it doesn't exist
            return vinylRepo.saveVinyl(newVinyl);
        }
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
    public boolean deleteVinylById(int id) {
        return vinylRepo.deleteVinylById(id);
    }
}
