package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Auction;

public interface AuctionService {

    Auction createAuction(Auction auction);

    Auction getAuctionsById(int id);

    List<Auction> getAuctions();

    boolean placeBid(int auctionId, double bidAmount);

}