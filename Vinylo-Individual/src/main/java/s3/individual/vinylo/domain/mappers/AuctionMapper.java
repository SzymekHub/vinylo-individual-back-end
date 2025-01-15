package s3.individual.vinylo.domain.mappers;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Component;
import s3.individual.vinylo.domain.dtos.AuctionDTO;
import s3.individual.vinylo.domain.dtos.AuctionsDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.services.BidService;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.Bid;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.User;

@Component
public class AuctionMapper {

    private static VinylService vinylService;
    private static UserService userService;
    private static BidService bidService;

    public AuctionMapper(VinylService vinylService, UserService userService, BidService bidService) {
        AuctionMapper.vinylService = vinylService;
        AuctionMapper.userService = userService;
        AuctionMapper.bidService = bidService;
    }

    // private AuctionMapper() {
    // throw new UnsupportedOperationException("Utility class");
    // }

    public static Auction toAuction(AuctionDTO auctionDTO) {
        if (auctionDTO == null) {
            return null;
        }

        if (vinylService == null) {
            throw new CustomNotFoundException("VinylService was not set in the AuctionMapper");
        }

        if (userService == null) {
            throw new CustomNotFoundException("UserService was not set in the AuctionMapper");
        }
        if (bidService == null) {
            throw new CustomNotFoundException("BidService was not set in the AuctionMapper");
        }

        Vinyl vinyl = vinylService.getVinylById(auctionDTO.getVinyl_id());

        User seller = userService.getUserById(auctionDTO.getSeller_id());

        Auction auction = new Auction(
                auctionDTO.getId(),
                auctionDTO.getTitle(),
                vinyl,
                seller,
                auctionDTO.getDescription(),
                auctionDTO.getStartingPrice(),
                auctionDTO.getCurrentPrice(),
                auctionDTO.getStartTime(),
                auctionDTO.getEndTime(),
                null);

        // update the auction status here
        updateAuctionStatus(auction);

        return auction;
    }

    public static AuctionDTO toAuctionDTO(Auction auction) {
        if (auction == null) {
            return null;
        }

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(auction.getId());
        auctionDTO.setTitle(auction.getTitle());
        auctionDTO.setVinyl_id(auction.getVinyl().getId());
        auctionDTO.setSeller_id(auction.getSeller().getId());
        auctionDTO.setDescription(auction.getDescription());
        auctionDTO.setStartingPrice(auction.getStartingPrice());
        auctionDTO.setCurrentPrice(auction.getCurrentPrice());
        auctionDTO.setStartTime(auction.getStartTime());
        auctionDTO.setEndTime(auction.getEndTime());

        return auctionDTO;
    }

    public static void updateAuctionStatus(Auction auction) {
        if (auction == null) {
            return;
        }

        if (auction.getEndTime().isBefore(LocalDate.now()) || auction.getEndTime().isEqual(LocalDate.now())) {
            Bid highestBid = bidService.getHighestBid(auction.getId());
            if (highestBid != null) {
                auction.setAuctionStatus("Closed - Won by user: "
                        + userService.getUserById(highestBid.getUserId()).getUsername() + ", for "
                        + highestBid.getBidAmount());
            } else {
                auction.setAuctionStatus("Closed - No bids");
            }
        } else if (auction.getStartTime().isAfter(LocalDate.now())) {
            auction.setAuctionStatus("Pending");
        } else {
            auction.setAuctionStatus("Open");
        }

    }

    public static AuctionDTO toAuctionDTOSummary(Auction auction) {
        if (auction == null) {
            return null;
        }

        AuctionDTO auctionDTO = new AuctionDTO();

        auctionDTO.setId(auction.getId());
        auctionDTO.setTitle(auction.getTitle());
        auctionDTO.setCurrentPrice(auction.getCurrentPrice());
        auctionDTO.setEndTime(auction.getEndTime());

        return auctionDTO;
    }

    public static AuctionsDTO toAuctionsSummaryDTO(List<Auction> auction) {
        AuctionsDTO dtos = new AuctionsDTO();
        for (Auction a : auction) {
            dtos.getAuctions().add(toAuctionDTOSummary(a));
        }

        return dtos;
    }

    public static AuctionsDTO toAuctionsDTO(List<Auction> auctions) {
        AuctionsDTO ad = new AuctionsDTO();
        for (Auction a : auctions) {
            ad.getAuctions().add(toAuctionDTO(a));
        }
        return ad;
    }

    public static List<Auction> toAuctions(AuctionsDTO ad) {
        List<Auction> auctions = new ArrayList<>();
        for (AuctionDTO a : ad.getAuctions()) {
            auctions.add(toAuction(a));
        }
        return auctions;
    }

}