package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Auction;

public interface AuctionService {

    Auction saveAuction(Integer id, Auction newAuction);

    Auction getAuctionsById(int id);

    List<Auction> getAuctions(int page, int size);

    int getTotalAuctionsCount();

    boolean placeBid(int auctionId, double bidAmount);

    boolean deativateAuctionById(int id);

}