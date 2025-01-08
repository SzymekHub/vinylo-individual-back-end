package s3.individual.vinylo.services;

import s3.individual.vinylo.domain.Profile;

public interface ProfileService {

    Profile saveProfile(Integer id, Profile profile);

    Profile getProfileById(int id);

}
