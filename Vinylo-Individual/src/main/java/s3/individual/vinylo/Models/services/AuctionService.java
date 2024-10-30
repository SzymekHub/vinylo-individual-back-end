package s3.individual.vinylo.Models.services;

import java.util.List;

import s3.individual.vinylo.serviceIMPL.domain.Auction;

public interface AuctionService {

    Auction createAuction(Auction auction);

    Auction getAuctionsById(int id);

    List<Auction> getAuctions();

    boolean placeBid(int auctionId, double bidAmount);

}