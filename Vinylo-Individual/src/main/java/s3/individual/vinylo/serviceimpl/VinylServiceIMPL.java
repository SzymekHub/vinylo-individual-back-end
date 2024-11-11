package s3.individual.vinylo.serviceimpl;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
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
        try {
            if (id != null) {
                // Check if the vinyl exists
                Vinyl existingVinyl = vinylRepo.getVinylById(id);
                if (existingVinyl == null) {
                    // Throw an exception if the vinyl doesn't exist
                    throw new CustomNotFoundException(
                            "Vinyl with ID " + id + " was not found.  A new vinyl will be created");
                }
                // Update the existing vinyl with the new details
                existingVinyl.setTitle(newVinyl.getTitle());
                existingVinyl.setDescription(newVinyl.getDescription());
                existingVinyl.setIsReleased(newVinyl.getisReleased());
                existingVinyl.setArtist(newVinyl.getArtist());
                existingVinyl.setVinylType(newVinyl.getvinylType());

                // Save the updated vinyl to the database
                return vinylRepo.saveVinyl(existingVinyl);

            } else {
                // If no ID is provided or vinyl doesn't exist, create a new one
                return vinylRepo.saveVinyl(newVinyl);
            }
        } catch (Exception e) {
            throw new CustomInternalServerErrorException("Failed to save the vinyl.");
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
