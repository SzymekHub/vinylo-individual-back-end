package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.Auction;

public interface AuctionRepo {

    List<Auction> getAuctions(int offset, int limit);

    Auction getAuctionByTitle(String title);

    Auction saveAuction(Auction auction);

    Auction findByVinylAndTitle(int vinylId, String title);

    Auction getAuctionById(int id);

    int getTotalAuctionsCount();

    boolean deativateAuctionById(int id);
}