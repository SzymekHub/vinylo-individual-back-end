package s3.individual.vinylo.persistence.jparepositoryimpl;

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

        // Check if id is null or 0, then save if it's not then update
        if (entity.getId() == 0) {
            ProfileEntity savedProfileEntity = profileJPARepo.save(entity);
            return ProfileEntityMapper.fromEntity(savedProfileEntity);
        }
        ProfileEntity mergedProfileEntity = entityManager.merge(entity);

        return ProfileEntityMapper.fromEntity(mergedProfileEntity);

    }

    @Override
    public Profile findById(int id) {
        return profileJPARepo.findById(id)
                .map(ProfileEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public Profile findByUserId(int userId) {
        return profileJPARepo.findByUserId(userId)
                .map(ProfileEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public UserEntity getUserByUserId(int id) {
        return profileJPARepo.findUserByUserId(id).orElse(null);
    }

    @Override
    public Profile findByBioAndUser(String bio, int userId) {
        return profileJPARepo.findByBioAndUser(bio, userId)
                .map(ProfileEntityMapper::fromEntity)
                .orElse(null);
    }
}