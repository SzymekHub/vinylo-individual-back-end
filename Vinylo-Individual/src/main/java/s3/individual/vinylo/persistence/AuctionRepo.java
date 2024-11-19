package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.Auction;

public interface AuctionRepo {

    List<Auction> getAuctions();

    Auction getAuctionByTitle(String title);

    Auction saveAuction(Auction auction);

    Auction getAuctionById(int id);

    boolean deativateAuctionById(int id);
}
