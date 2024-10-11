package s3.individual.vinylo.Models.Mappers;


import java.util.*;
import s3.individual.vinylo.Models.dtos.AuctionDTO;
import s3.individual.vinylo.Models.dtos.AuctionsDTO;
import s3.individual.vinylo.services.domain.Auction;

public class AuctionMapper {

    public static Auction toAuction(AuctionDTO auctionDTO) {
        if (auctionDTO == null) {
            return null;
        }

        return new Auction(
            auctionDTO.getId(),
            auctionDTO.getTitle(),
            VinylMapper.toVinyl(auctionDTO.getVinyl()),
            UserMapper.toUser(auctionDTO.getSeller()),
            auctionDTO.getDescription(),
            auctionDTO.getStartingPrice(),
            auctionDTO.getCurrentPrice(),
            auctionDTO.getStartTime(),
            auctionDTO.getEndTime()
        );
    }

    public static AuctionDTO toAuctionDTO(Auction auction) {
        if (auction == null) {
            return null;
        }
        
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(auction.getId());
        auctionDTO.setTitle(auction.getTitle());
        auctionDTO.setVinyl(VinylMapper.toVinylDTO(auction.getVinyl())); 
        auctionDTO.setSeller(UserMapper.toUserDTO(auction.getSeller())); 
        auctionDTO.setDescription(auction.getDescription());
        auctionDTO.setStartingPrice(auction.getStartingPrice());
        auctionDTO.setCurrentPrice(auction.getCurrentPrice());
        auctionDTO.setStartTime(auction.getStartTime());
        auctionDTO.setEndTime(auction.getEndTime());
        return auctionDTO;
    }

    public static AuctionsDTO toAuctionsDTO(List<Auction> auctions) {
      AuctionsDTO ad = new AuctionsDTO();
      for (Auction a: auctions) {
        ad.auctions.add(toAuctionDTO(a));
      }
      return ad;
    }

    public static List<Auction> tAuctions(AuctionsDTO ad) {
        List<Auction> auctions = new ArrayList<>();
        for (AuctionDTO a: ad.auctions) {
          auctions.add(toAuction(a));
        }
        return auctions;
    }

}
