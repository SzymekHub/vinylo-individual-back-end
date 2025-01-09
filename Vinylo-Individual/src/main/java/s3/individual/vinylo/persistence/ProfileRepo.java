package s3.individual.vinylo.persistence;

import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.persistence.entity.UserEntity;

public interface ProfileRepo {

    Profile findById(int id);

    Profile saveProfile(Profile profile);

    UserEntity getProfileById(int id);

    Profile findByBioAndUser(String bio, int user_id);
}
