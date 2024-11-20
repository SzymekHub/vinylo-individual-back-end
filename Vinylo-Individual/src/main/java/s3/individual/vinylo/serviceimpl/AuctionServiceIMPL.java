package s3.individual.vinylo.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.persistence.AuctionRepo;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;

@Service
@RequiredArgsConstructor
public class AuctionServiceIMPL implements AuctionService {

    private final AuctionRepo auctionRepo;

    @Override
    @Transactional
    public Auction saveAuction(Integer id, Auction newAuction) {
        try {
            if (id != null) {
                Auction existingAuction = auctionRepo.getAuctionById(id);
                if (existingAuction == null) {
                    throw new CustomNotFoundException(
                            "Auction with ID " + id + " was not found. A new Auction will be created");
                }
                // Update the existing auction with the new details
                existingAuction.setDescription(newAuction.getDescription());
                existingAuction.setCurrentPrice(newAuction.getCurrentPrice());
                existingAuction.setEndTime(existingAuction.getEndTime());

                return auctionRepo.saveAuction(existingAuction);
            } else {
                return auctionRepo.saveAuction(newAuction);
            }
        } catch (Exception ex) {
            throw new CustomInternalServerErrorException("Failed to save the auction " + ex.getMessage());
        }
    }

    @Override
    public Auction getAuctionsById(int id) {

        return auctionRepo.getAuctionById(id);
    }

    @Override
    public List<Auction> getAuctions() {

        return auctionRepo.getAuctions();
    }

    @Override
    public boolean placeBid(int auctionId, double bidAmount) {
        Auction auction = auctionRepo.getAuctionById(auctionId);
        if (auction != null && bidAmount > auction.getCurrentPrice()) {
            auction.setCurrentPrice(bidAmount);
            auctionRepo.saveAuction(auction);
            return true;
        }
        return false;
    }

    @Override
    public boolean deativateAuctionById(int id) {
        return auctionRepo.deativateAuctionById(id);
    }
}
