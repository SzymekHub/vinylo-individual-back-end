package s3.individual.vinylo.services.interfaces;

import java.util.ArrayList;

import s3.individual.vinylo.services.domain.Auction;

public interface AuctionRepo {
    Auction save (Auction auction);;

    ArrayList<Auction> getAuctions();

    Auction getAuctionById(int id);
}
