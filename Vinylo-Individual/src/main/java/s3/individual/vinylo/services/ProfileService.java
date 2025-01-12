package s3.individual.vinylo.services;

import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.dtos.ProfileAndUserDTO;
import s3.individual.vinylo.domain.dtos.ProfileDTO;

public interface ProfileService {

    Profile createProfile(Profile newProfile);

    Profile updateProfile(Integer userId, ProfileDTO profileDTO);

    Profile upgradeToPremium(int userId);

    ProfileAndUserDTO getProfileAndUserById(int id);

}
