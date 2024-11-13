package s3.individual.vinylo.persistence.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import s3.individual.vinylo.persistence.entity.UserEntity;

public interface UserJPARepo extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);

}
