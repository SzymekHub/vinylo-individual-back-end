package s3.individual.vinylo.Models.persistence;

import java.util.*;

import s3.individual.vinylo.serviceIMPL.domain.Auction;

public interface AuctionRepo {

    List<Auction> getAuctions();
    
    Auction createNewAuction (Auction auction);;

    Auction getAuctionById(int id);
}
