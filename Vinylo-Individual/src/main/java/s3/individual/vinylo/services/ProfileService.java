package s3.individual.vinylo.services;

import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.dtos.ProfileAndUserDTO;

public interface ProfileService {

    Profile saveProfile(Integer id, Profile profile);

    ProfileAndUserDTO getProfileAndUserById(int id);

}
