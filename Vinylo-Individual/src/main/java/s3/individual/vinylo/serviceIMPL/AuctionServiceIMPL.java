package s3.individual.vinylo.serviceIMPL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.AuctionRepo;
import s3.individual.vinylo.Models.services.AuctionService;
import s3.individual.vinylo.serviceIMPL.domain.Auction;

@Service
public class AuctionServiceIMPL implements AuctionService {
    
    private final AuctionRepo auctionRepo;

    @Autowired
    public AuctionServiceIMPL(AuctionRepo auctionRepo) {
        
        this.auctionRepo = auctionRepo;
    }
    @Override
    public Auction createAuction(Auction auction){

        return auctionRepo.createNewAuction(auction);
    }

    @Override
    public Auction getAuctionsById(int id) {

        return auctionRepo.getAuctionById(id);
    }

    @Override
    public List<Auction> getAuctions(){

        return auctionRepo.getAuctions();
    }

    @Override
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
