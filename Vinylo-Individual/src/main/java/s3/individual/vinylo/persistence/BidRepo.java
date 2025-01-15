package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.Bid;

public interface BidRepo {
    Bid saveBid(Bid bid);

    List<Bid> getBidsByAuctionId(int auctionId);
}