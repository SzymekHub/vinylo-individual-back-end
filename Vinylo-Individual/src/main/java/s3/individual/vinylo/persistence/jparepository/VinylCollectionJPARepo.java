package s3.individual.vinylo.persistence.jparepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import s3.individual.vinylo.persistence.entity.VinylCollectionEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;

public interface VinylCollectionJPARepo extends JpaRepository<VinylCollectionEntity, Integer> {

    boolean existsById(int id);

    // Finds a specific vinyl in the collection of a specific user (ids)
    @Query("SELECT vc FROM VinylCollectionEntity vc WHERE vc.user.id = :userId AND vc.vinyl.id = :vinylId")
    Optional<VinylCollectionEntity> findByUserAndVinyl(
            @Param("userId") int userId,
            @Param("vinylId") int vinylId);

    // Retrieves all vinyl records associated with a specific user
    @Query("SELECT vc.vinyl FROM VinylCollectionEntity vc WHERE vc.user.id = :userId")
    List<VinylEntity> findByUserId(@Param("userId") int userId);

    // Finds a specific vinyl in the user's collection (corrected for clarity)
    @Query("SELECT vc.vinyl FROM VinylCollectionEntity vc WHERE vc.user.id = :userId AND vc.vinyl.id = :vinylId")
    Optional<VinylEntity> findByUserIdAndVinylId(
            @Param("userId") int userId,
            @Param("vinylId") int vinylId);

}
