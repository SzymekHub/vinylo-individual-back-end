package s3.individual.vinylo.persistence.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import s3.individual.vinylo.persistence.entity.BidEntity;

public interface BidJPARepo extends JpaRepository<BidEntity, Integer> {

    @Query("SELECT b FROM BidEntity b WHERE b.auction.id = :auctionId ORDER BY b.bidAmount DESC")
    List<BidEntity> findByAuctionIdOrderByBidAmountDesc(@Param("auctionId") int auctionId);
}