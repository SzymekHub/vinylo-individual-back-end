package s3.individual.vinylo.persistence.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import s3.individual.vinylo.persistence.entity.ProfileEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;

public interface ProfileJPARepo extends JpaRepository<ProfileEntity, Integer> {

    // Retrieves all user info associated with a specific user
    @Query("SELECT pf.user FROM ProfileEntity pf WHERE pf.user.id = :userId")
    Optional<UserEntity> findByUserId(@Param("userId") int userId);

    // Find profile by bio and user id
    @Query("SELECT p FROM ProfileEntity p WHERE p.bio = :bio AND p.user.id = :userId")
    Optional<ProfileEntity> findByBioAndUser(
            @Param("bio") String bio,
            @Param("userId") int userId);
}
