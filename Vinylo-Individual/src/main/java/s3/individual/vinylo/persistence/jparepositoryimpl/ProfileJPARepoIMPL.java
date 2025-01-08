package s3.individual.vinylo.persistence.jparepositoryIMPL;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.mappers.ProfileEntityMapper;
import s3.individual.vinylo.persistence.ProfileRepo;
import s3.individual.vinylo.persistence.entity.ProfileEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.ProfileJPARepo;

@Repository
public class ProfileJPARepoIMPL implements ProfileRepo {

    private final ProfileJPARepo profileJPARepo;

    @PersistenceContext
    private EntityManager entityManager;

    public ProfileJPARepoIMPL(ProfileJPARepo profileJPARepo) {
        this.profileJPARepo = profileJPARepo;
    }

    @Override
    @Transactional
    public Profile saveProfile(Profile profile) {
        ProfileEntity entity = ProfileEntityMapper.toEntity(profile);
        UserEntity managedEntity = entityManager.merge(entity.getUser());
        entity.setUser(managedEntity);
        ProfileEntity savedProfileEntity = profileJPARepo.save(entity);
        return ProfileEntityMapper.fromEntity(savedProfileEntity);
    }

    @Override
    public Profile getProfileById(int id) {
        return profileJPARepo.findById(id)
                .map(ProfileEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public Profile findByBioAndUser(String bio, int user_id) {
        return profileJPARepo.findByBioAndUser(bio, user_id)
                .map(ProfileEntityMapper::fromEntity)
                .orElse(null);
    }
}
