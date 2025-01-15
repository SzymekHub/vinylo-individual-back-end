package s3.individual.vinylo.domain.mappers;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.Bid;
import s3.individual.vinylo.domain.dtos.BidDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.services.UserService;

@Component
public class BidMapper {

    private static UserService userService;
    private static AuctionService auctionService;

    public BidMapper(UserService userService, AuctionService auctionService) {
        BidMapper.userService = userService;
        BidMapper.auctionService = auctionService;
    }

    public static Bid toBid(BidDTO bidDTO) {

        if (bidDTO == null) {
            return null;
        }

        if (userService == null) {
            throw new CustomNotFoundException("UserService not set in BidMapper");
        }

        if (auctionService == null) {
            throw new CustomNotFoundException("AuctionService not set in BidMapper");
        }

        return Bid.builder()
                .id(bidDTO.getId())
                .auctionId(bidDTO.getAuctionId())
                .userId(bidDTO.getUserId())
                .bidAmount(bidDTO.getBidAmount())
                .bidTime(bidDTO.getBidTime())
                .build();
    }

    public static BidDTO toBidDTO(Bid bid) {

        if (bid == null) {
            return null;
        }

        return BidDTO.builder()
                .id(bid.getId())
                .auctionId(bid.getAuctionId())
                .userId(bid.getUserId())
                .bidAmount(bid.getBidAmount())
                .bidTime(bid.getBidTime())
                .build();
    }
}