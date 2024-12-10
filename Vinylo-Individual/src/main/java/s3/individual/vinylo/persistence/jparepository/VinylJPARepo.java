package s3.individual.vinylo.persistence.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import s3.individual.vinylo.persistence.entity.VinylEntity;

public interface VinylJPARepo extends JpaRepository<VinylEntity, Integer> {

    boolean existsById(int id);

    @Query("SELECT COUNT(v) FROM VinylEntity v")
    int getTotalVinylsCount();

    @Query("SELECT v FROM VinylEntity v WHERE v.artist.id = :artistId AND v.title = :title AND v.state = :state")
    Optional<VinylEntity> findByArtistIdAndTitleAndState(
            @Param("artistId") int artistId,
            @Param("title") String title,
            @Param("state") String state);

}
