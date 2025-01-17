package s3.individual.vinylo.persistence.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import s3.individual.vinylo.persistence.entity.ArtistEntity;

public interface ArtistJPARepo extends JpaRepository<ArtistEntity, Integer> {

    boolean existsById(int id);

    // The method defines a query to find an ArtistEntity in the database where the
    // name field matches the provided name argument.
    Optional<ArtistEntity> findByName(String name);

    // It allows you to handle the "not found" case in a cleaner way, such as using
    // map(), orElse(), or ifPresent() methods provided by Optional.
    @Modifying
    @Transactional
    @Query("DELETE FROM ArtistEntity a WHERE a.id = :artistId")
    void deleteByArtistId(@Param("artistId") int artistId);

    @Query("SELECT COUNT(a) FROM ArtistEntity a")
    int getTotalArtistsCount();
}
