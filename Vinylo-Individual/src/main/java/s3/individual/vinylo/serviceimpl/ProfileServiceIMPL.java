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
    public Profile createProfile(Profile newProfile) {
        try {
            // Check if profile with the same bio and user exists
            Profile existingProfile = profileRepo.findByBioAndUser(
                    newProfile.getBio(),
                    newProfile.getUser().getId());
            if (existingProfile != null) {
                throw new DuplicateItemException(
                        "A profile with the same bio already exists for this user.");
            }

            // if no id is provided, create a new profile
            return profileRepo.saveProfile(newProfile);

        } catch (DuplicateItemException e) {
            throw e;
        } catch (Exception e) {
            // Catch other exceptions and wrap them in a CustomInternalServerErrorException
            throw new CustomInternalServerErrorException("Failed to save the profile. " + e.toString());
        }
    }

    @Override
    @Transactional
    public ProfileDTO updateProfile(Integer userId, ProfileDTO profileDTO) {
        try {
            Profile profileToUpdate = profileRepo.findByUserId(userId);

            if (profileToUpdate == null) {
                throw new CustomNotFoundException("Profile with User ID: " + userId + " was not found");
            }

            // set the id so that it won't think that it's a new record.
            profileToUpdate.setId(profileToUpdate.getId());

            // Update the existing profile with the new data
            profileToUpdate.setBalance(profileDTO.getBalance());
            profileToUpdate.setBio(profileDTO.getBio());

            Profile updatedProfile = profileRepo.saveProfile(profileToUpdate);
            return ProfileMapper.toProfileDTO(updatedProfile);

        } catch (CustomNotFoundException e) {
            // Re-throw the CustomNotFoundException
            throw e;
        } catch (Exception e) {
            // Catch other exceptions and wrap them in a CustomInternalServerErrorException
            throw new CustomInternalServerErrorException("Failed to update the profile. " + e.toString());
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