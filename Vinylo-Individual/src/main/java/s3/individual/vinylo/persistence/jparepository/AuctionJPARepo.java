package s3.individual.vinylo.persistence.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import s3.individual.vinylo.persistence.entity.AuctionEntity;

public interface AuctionJPARepo extends JpaRepository<AuctionEntity, Integer> {
    boolean existsById(int id);

    Optional<AuctionEntity> findByTitle(String title);

    @Query("SELECT COUNT(a) FROM AuctionEntity a")
    int getTotalAuctionsCount();

    @Query("SELECT a FROM AuctionEntity a WHERE a.vinyl.id = :vinylId AND a.title = :title")
    Optional<AuctionEntity> findByVinylAndTitle(
            @Param("vinylId") int vinylId,
            @Param("title") String title);

}
