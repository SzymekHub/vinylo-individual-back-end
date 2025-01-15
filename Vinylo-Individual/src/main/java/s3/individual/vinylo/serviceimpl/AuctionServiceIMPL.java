package s3.individual.vinylo.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.exceptions.DuplicateItemException;
import s3.individual.vinylo.persistence.AuctionRepo;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.domain.mappers.AuctionMapper;

@Service
@RequiredArgsConstructor
public class AuctionServiceIMPL implements AuctionService {

    private final AuctionRepo auctionRepo;

    @Override
    @Transactional
    public Auction saveAuction(Integer id, Auction newAuction) {
        try {
            if (id != null) {
                Auction auctionToUpdate = auctionRepo.getAuctionById(id);
                if (auctionToUpdate == null) {
                    throw new CustomNotFoundException(
                            "Auction with ID " + id + " was not found.");
                }

                // Check if a auction with the same vinyl and title exists(excluding the current
                // ID)

                Auction existingAuction = auctionRepo.findByVinylAndTitle(
                        newAuction.getVinyl().getId(),
                        newAuction.getTitle());
                if (existingAuction != null && !existingAuction.getId().equals(id)) {
                    throw new DuplicateItemException(
                            "An auction with the same title and vinyl already exists.");
                }

                // Update the existing auction with the new details
                auctionToUpdate.setTitle(newAuction.getTitle());
                auctionToUpdate.setDescription(newAuction.getDescription());
                auctionToUpdate.setCurrentPrice(newAuction.getCurrentPrice());
                auctionToUpdate.setStartTime(newAuction.getStartTime());
                auctionToUpdate.setEndTime(newAuction.getEndTime());
                auctionToUpdate.setAuctionStatus(newAuction.getAuctionStatus());

                return auctionRepo.saveAuction(auctionToUpdate);

            } else {
                Auction existingAuction = auctionRepo.findByVinylAndTitle(
                        newAuction.getVinyl().getId(),
                        newAuction.getTitle());
                if (existingAuction != null && !existingAuction.getId().equals(id)) {
                    throw new DuplicateItemException(
                            "An auction with the same title and vinyl already exists.");
                }

                return auctionRepo.saveAuction(newAuction);
            }
        } catch (DuplicateItemException e) {
            // Re-throw the DuplicateVinylException
            throw e;
        } catch (CustomNotFoundException e) {
            // Re-throw the CustomNotFoundException
            throw e;
        } catch (Exception ex) {
            throw new CustomInternalServerErrorException("Failed to save the auction " + ex.toString());
        }
    }

    @Override
    public Auction getAuctionsById(int id) {
        return auctionRepo.getAuctionById(id);
    }

    @Override
    public List<Auction> getAuctions(int page, int size) {
        int offset = page * size;
        List<Auction> auctions = auctionRepo.getAuctions(offset, size);
        return auctions.stream()
                .map(auction -> {
                    // update the auction status in the mapper
                    AuctionMapper.updateAuctionStatus(auction);
                    return auction;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalAuctionsCount() {
        return auctionRepo.getTotalAuctionsCount();
    }

    @Override
    public boolean deativateAuctionById(int id) {
        return auctionRepo.deativateAuctionById(id);
    }
}