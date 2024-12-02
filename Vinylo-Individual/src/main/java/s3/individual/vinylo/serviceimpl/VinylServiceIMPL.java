package s3.individual.vinylo.serviceimpl;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.exceptions.DuplicateVinylException;
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
            // If the ID is provided, attempt to update the existing vinyl
            if (id != null) {

                // Check if a vinyl with the same title, artist, and description already exists
                Vinyl existingVinyl = vinylRepo.getByState(
                        newVinyl.getState());

                // If a vinyl with the same details exists, throw a DuplicateVinylException
                if (existingVinyl != null) {
                    throw new DuplicateVinylException(
                            "Vinyl already exists in this state.");
                }

                // Check if the vinyl exists in the database
                Vinyl vinylToUpdate = vinylRepo.getVinylById(id);
                if (vinylToUpdate == null) {
                    throw new CustomNotFoundException(
                            "Vinyl with ID " + id + " was not found. A new vinyl will be created.");
                }

                // Existing vinyl logic...

                // Update the existing vinyl with the new data
                vinylToUpdate.setTitle(newVinyl.getTitle());
                vinylToUpdate.setDescription(newVinyl.getDescription());
                vinylToUpdate.setIsReleased(newVinyl.getIsReleased());
                vinylToUpdate.setVinylType(newVinyl.getVinylType());
                // vinylToUpdate.setArtist(newVinyl.getArtist());

                // Save the updated vinyl to the database
                return vinylRepo.saveVinyl(vinylToUpdate);

            } else {
                // If no ID is provided, create a new vinyl
                return vinylRepo.saveVinyl(newVinyl);

            }
        } catch (Exception e) {
            throw new CustomInternalServerErrorException("Failed to save the vinyl." + e.toString());
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
