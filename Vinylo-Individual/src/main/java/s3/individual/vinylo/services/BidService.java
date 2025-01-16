package s3.individual.vinylo.services;

import s3.individual.vinylo.domain.Bid;

public interface BidService {

    Bid placeBid(int auctionId, int userId, double bidAmount);

    Bid getHighestBid(int auctionId);
}