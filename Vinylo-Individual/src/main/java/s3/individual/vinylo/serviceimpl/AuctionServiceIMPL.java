package s3.individual.vinylo.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.dtos.AuctionDTO;
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
    public Auction createAuction(Auction newAuction) {
        try {

            Auction existingAuction = auctionRepo.findByVinylAndTitle(
                    newAuction.getVinyl().getId(),
                    newAuction.getTitle());
            if (existingAuction != null) {
                throw new DuplicateItemException(
                        "An auction with the same title and vinyl already exists.");
            }

            return auctionRepo.saveAuction(newAuction);
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
    @Transactional
    public Auction updateAuction(Integer id, AuctionDTO auctionDTO) {
        try {
            Auction auctionToUpdate = auctionRepo.getAuctionById(id);

            if (auctionToUpdate == null) {
                throw new CustomNotFoundException("Auction with ID: " + id + " was not found.");
            }

            // I set the id so that it won't think it's a new auction
            auctionToUpdate.setId(auctionToUpdate.getId());

            auctionToUpdate.setTitle(auctionDTO.getTitle());
            auctionToUpdate.setDescription(auctionDTO.getDescription());
            auctionToUpdate.setStartingPrice(auctionDTO.getStartingPrice());
            auctionToUpdate.setCurrentPrice(auctionDTO.getCurrentPrice());
            auctionToUpdate.setStartTime(auctionDTO.getStartTime());
            auctionToUpdate.setEndTime(auctionDTO.getEndTime());

            return auctionRepo.saveAuction(auctionToUpdate);

        } catch (CustomNotFoundException e) {
            // Re-throw the CustomNotFoundException
            throw e;
        } catch (Exception e) {
            // Catch other exceptions and wrap them in a CustomInternalServerErrorException
            throw new CustomInternalServerErrorException("Failed to update the auction. " + e.toString());
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