package s3.individual.vinylo.persistence.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import s3.individual.vinylo.persistence.entity.ProfileEntity;

public interface ProfileJPARepo extends JpaRepository<ProfileEntity, Integer> {

    boolean existsById(int id);

    // Retrieves all user info associated with a specific user
    @Query("SELECT p.user FROM ProfileEntity p WHERE p.user.id = :userId")
    Optional<ProfileEntity> findByUserId(@Param("userId") int userId);

    // Find profile by bio and user id
    @Query("SELECT p FROM ProfileEntity p WHERE p.bio = :bio AND p.user.id = :userId")
    Optional<ProfileEntity> findByBioAndUser(
            @Param("bio") String bio,
            @Param("userId") int userId);
}
