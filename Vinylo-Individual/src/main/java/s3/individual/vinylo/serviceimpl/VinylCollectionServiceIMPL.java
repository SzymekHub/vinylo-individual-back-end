package s3.individual.vinylo.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.exceptions.DuplicateItemException;
import s3.individual.vinylo.persistence.VinylCollectionRepo;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.services.VinylCollectionService;

@Service
@RequiredArgsConstructor
public class VinylCollectionServiceIMPL implements VinylCollectionService {

    private final VinylCollectionRepo vinylCollectionRepo;

    @Override
    @Transactional
    public VinylCollection addVinylToCollection(Integer id, VinylCollection newvinylCollection) {

        try {
            if (id != null) {
                VinylCollection collectionToUpdate = vinylCollectionRepo.findById(id);
                if (collectionToUpdate == null) {
                    throw new CustomNotFoundException("Collection with ID " + id + " was not found.");
                }

                VinylCollection existingCollection = vinylCollectionRepo.findByUserAndVinyl(
                        newvinylCollection.getUser().getId(),
                        newvinylCollection.getVinyl().getId());
                if (existingCollection != null && !existingCollection.getId().equals(id)) {
                    throw new DuplicateItemException(
                            "User already has this vinyl in their collection.");
                }

                // Existing vinyl logic...

                // Update the existing collection with the new data
                collectionToUpdate.setUser(newvinylCollection.getUser());
                collectionToUpdate.setVinyl(newvinylCollection.getVinyl());

                return vinylCollectionRepo.save(collectionToUpdate);
            } else {
                VinylCollection existingCollection = vinylCollectionRepo.findByUserAndVinyl(
                        newvinylCollection.getUser().getId(),
                        newvinylCollection.getVinyl().getId());
                if (existingCollection != null) {
                    throw new DuplicateItemException(
                            "User already has this vinyl in their collection.");
                }
                return vinylCollectionRepo.save(newvinylCollection);
            }
        } catch (DuplicateItemException e) {
            // Re-throw the DuplicateVinylException
            throw e;
        } catch (CustomNotFoundException e) {
            // Re-throw the CustomNotFoundException
            throw e;
        } catch (Exception e) {
            // Catch other exceptions and wrap them in a CustomInternalServerErrorException
            throw new CustomInternalServerErrorException("Failed to save the collection. " + e.toString());
        }
    }

    @Override
    public List<VinylCollection> getAllCollections() {
        return vinylCollectionRepo.findAll();
    }

    @Override
    public VinylCollection getCollectionById(int id) {
        return vinylCollectionRepo.findById(id);
    }

    @Override
    public List<VinylEntity> getCollectionByUserId(int userId) {
        return vinylCollectionRepo.findByUserId(userId);
    }

    @Override
    public VinylEntity getByUserIdAndVinylId(int userId, int vinylId) {
        return vinylCollectionRepo.findByUserIdAndVinylId(userId, vinylId);
    }

    @Override
    public boolean deleteCollection(int vinylId, int userId) {
        return vinylCollectionRepo.deleteByVinylIdAndUserId(vinylId, userId);
    }
}
