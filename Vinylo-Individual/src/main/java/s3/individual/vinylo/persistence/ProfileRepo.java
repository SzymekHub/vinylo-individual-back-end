package s3.individual.vinylo.persistence;

import s3.individual.vinylo.domain.Profile;

public interface ProfileRepo {

    Profile saveProfile(Profile profile);

    Profile getProfileById(int id);

    Profile findByBioAndUser(String bio, int user_id);
}
