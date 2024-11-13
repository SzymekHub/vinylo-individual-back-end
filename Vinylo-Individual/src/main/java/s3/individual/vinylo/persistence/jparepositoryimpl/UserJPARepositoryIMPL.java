package s3.individual.vinylo.persistence.jparepositoryIMPL;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import s3.individual.vinylo.domain.mappers.entities.UserEntityMapper;
import s3.individual.vinylo.persistence.UserRepo;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;

@Repository
public class UserJPARepositoryIMPL implements UserRepo {

    private final UserJPARepo userJPARepo;

    @PersistenceContext
    private EntityManager entityManager;

    public UserJPARepositoryIMPL(UserJPARepo userJPARepo) {
        this.userJPARepo = userJPARepo;
    }

    @Override
    public List<User> getUsers() {
        return userJPARepo.findAll().stream()
                .map(UserEntityMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(int id) {
        // Look for a UserEntity in the database by its ID
        return userJPARepo.findById(id)
                // If found, map the UserEntity to a User domain object using
                // UserEntityMapper
                .map(UserEntityMapper::fromEntity)
                // If not found, return null
                .orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        // Look for a UserEntity in the database by its ID
        return userJPARepo.findByUsername(username).stream()
                .findFirst()
                // If found, map the UserEntity to a User domain object using
                // UserEntityMapper
                .map(UserEntityMapper::fromEntity)
                // If not found, return null
                .orElse(null);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        UserEntity savedEntity = userJPARepo.save(entity);
        return UserEntityMapper.fromEntity(savedEntity);
    }

    @Override
    public boolean deativateUserById(int id) {
        if (userJPARepo.existsById(id)) {
            userJPARepo.deleteById(id);
            return true;
        }
        return false;
    }
}
