package s3.individual.vinylo.domain.mappers;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.entity.ProfileEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;

@Component
public class ProfileEntityMapper {

    private static UserJPARepo userJPARepo;

    public ProfileEntityMapper(UserJPARepo userJPARepo) {
        ProfileEntityMapper.userJPARepo = userJPARepo;
    }

    public static ProfileEntity toEntity(Profile profile) {

        if (profile == null) {
            return null;
        }

        ProfileEntity profileEntity = new ProfileEntity();

        profileEntity.setId(profile.getId());
        if (profile.getUser() != null && profile.getUser().getId() != null) {
            UserEntity userEntity = userJPARepo.findById(profile.getUser().getId()).orElseThrow(
                    () -> new CustomNotFoundException("UserEntity not found in ProfileMapper"));
            profileEntity.setUser(userEntity);
        }

        profileEntity.setBio(profile.getBio());
        profileEntity.setBalance(profile.getBalance());

        return profileEntity;
    }

    public static Profile fromEntity(ProfileEntity profileEntity) {

        if (profileEntity == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setId(profileEntity.getId());
        profile.setUser(UserEntityMapper.fromEntity(profileEntity.getUser()));
        profile.setBio(profileEntity.getBio());
        profile.setBalance(profileEntity.getBalance());

        return profile;
    }

}
