package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.dtos.AuctionDTO;

public interface AuctionService {

    Auction createAuction(Auction newAuction);

    Auction updateAuction(Integer auctionId, AuctionDTO updatedAuction);

    Auction getAuctionsById(int id);

    List<Auction> getAuctions(int page, int size);

    int getTotalAuctionsCount();

    boolean deativateAuctionById(int id);

}