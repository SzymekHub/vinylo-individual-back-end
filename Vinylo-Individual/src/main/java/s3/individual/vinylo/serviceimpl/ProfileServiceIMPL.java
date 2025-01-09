package s3.individual.vinylo.serviceimpl;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.dtos.ProfileAndUserDTO;
import s3.individual.vinylo.domain.dtos.ProfileDTO;
import s3.individual.vinylo.domain.dtos.UserDTO;
import s3.individual.vinylo.domain.mappers.ProfileMapper;
import s3.individual.vinylo.domain.mappers.UserEntityMapper;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.exceptions.DuplicateItemException;
import s3.individual.vinylo.persistence.ProfileRepo;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.services.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceIMPL implements ProfileService {

    private final ProfileRepo profileRepo;

    @Override
    @Transactional
    public Profile saveProfile(Integer id, Profile newProfile) {
        try {
            if (id != null) {
                Profile profileToUpdate = profileRepo.findById(id);
                if (profileToUpdate == null) {
                    throw new CustomNotFoundException(
                            "Profile with ID " + id + " was not found.");
                }

                // Check if profile with the same bio and user exists
                Profile existingProfile = profileRepo.findByBioAndUser(
                        newProfile.getBio(),
                        newProfile.getUser().getId());
                if (existingProfile != null && !existingProfile.getId().equals(id)) {
                    throw new DuplicateItemException(
                            "A profile with the same bio already exists for this user.");
                }

                // Update the existing profile with the new data
                profileToUpdate.setBalance(newProfile.getBalance());
                profileToUpdate.setBio(newProfile.getBio());

                // Save the updated profile to the database
                return profileRepo.saveProfile(profileToUpdate);

            } else {
                // Check if profile with the same bio and user exists
                Profile existingProfile = profileRepo.findByBioAndUser(
                        newProfile.getBio(),
                        newProfile.getUser().getId());
                if (existingProfile != null && !existingProfile.getId().equals(id)) {
                    throw new DuplicateItemException(
                            "A profile with the same bio already exists for this user.");
                }

                // if no id is provided, create a new profile
                return profileRepo.saveProfile(newProfile);
            }
        } catch (DuplicateItemException e) {
            throw e;
        } catch (CustomNotFoundException e) {
            // Re-throw the CustomNotFoundException
            throw e;
        } catch (Exception e) {
            // Catch other exceptions and wrap them in a CustomInternalServerErrorException
            throw new CustomInternalServerErrorException("Failed to save the profile. " + e.toString());
        }
    }

    @Override
    public ProfileAndUserDTO getProfileAndUserById(int userId) {

        Profile profile = profileRepo.findByUserId(userId);
        if (profile == null) {
            throw new CustomNotFoundException("Profile not found for User ID: " + userId);
        }

        UserEntity userEntity = profileRepo.getUserByUserId(userId);
        if (userEntity == null) {
            throw new CustomNotFoundException("User not found for User ID: " + userId);
        }

        UserDTO userDTO = UserEntityMapper.toProfileDTO(userEntity);
        ProfileDTO profileDTO = ProfileMapper.toProfileDTO(profile);

        return new ProfileAndUserDTO(profileDTO, userDTO);
    }
}
