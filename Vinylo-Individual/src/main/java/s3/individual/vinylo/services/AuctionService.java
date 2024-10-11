package s3.individual.vinylo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.AuctionRepo;
import s3.individual.vinylo.services.domain.Auction;

@Service
public class AuctionService {
    
    private final AuctionRepo auctionRepo;

    @Autowired
    public AuctionService(AuctionRepo auctionRepo) {
        
        this.auctionRepo = auctionRepo;
    }
    public Auction createAuction(Auction auction){

        return auctionRepo.createNewAuction(auction);
    }

    public Auction getAuctionsById(int id) {

        return auctionRepo.getAuctionById(id);
    }

    public List<Auction> getAuctions(){

        return auctionRepo.getAuctions();
    }

    public boolean placeBid(int auctionId, double bidAmount) {
        Auction auction = auctionRepo.getAuctionById(auctionId);
        if (auction != null && bidAmount > auction.getCurrentPrice()) {
            auction.setCurrentPrice(bidAmount);
            auctionRepo.createNewAuction(auction);
            return true;
        }
        return false;
    }
}
