package s3.individual.vinylo.persistence;

import java.util.*;

import s3.individual.vinylo.domain.Auction;

public interface AuctionRepo {

    List<Auction> getAuctions();

    Auction createNewAuction(Auction auction);

    Auction getAuctionById(int id);
}
