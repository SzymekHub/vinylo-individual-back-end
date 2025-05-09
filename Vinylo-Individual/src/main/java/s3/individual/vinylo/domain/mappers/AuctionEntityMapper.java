package s3.individual.vinylo.domain.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.dtos.AuctionDTO;
import s3.individual.vinylo.domain.dtos.AuctionsDTO;
import s3.individual.vinylo.persistence.entity.AuctionEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.VinylJPARepo;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;
import s3.individual.vinylo.exceptions.CustomNotFoundException;

@Component
public class AuctionEntityMapper {

    private static VinylJPARepo vinylJPARepo;
    private static UserJPARepo userJPARepo;

    public AuctionEntityMapper(VinylJPARepo vinylJPARepo, UserJPARepo userJPARepo) {
        AuctionEntityMapper.vinylJPARepo = vinylJPARepo;
        AuctionEntityMapper.userJPARepo = userJPARepo;
    }

    // Converts Auction domain object to AuctionEntity
    public static AuctionEntity toEntity(Auction auction) {
        if (auction == null) {
            return null;
        }

        AuctionEntity auctionEntity = new AuctionEntity();

        auctionEntity.setId(auction.getId());
        auctionEntity.setTitle(auction.getTitle());
        auctionEntity.setDescription(auction.getDescription());
        auctionEntity.setStartingPrice(auction.getStartingPrice());
        auctionEntity.setCurrentPrice(auction.getCurrentPrice());
        auctionEntity.setStartTime(auction.getStartTime());
        auctionEntity.setEndTime(auction.getEndTime());
        auctionEntity.setAuctionStatus(auction.getAuctionStatus());

        // Handling the vinyl entity correctly
        if (auction.getVinyl() != null && auction.getVinyl().getId() != null) {
            VinylEntity vinylEntity = vinylJPARepo.findById(auction.getVinyl().getId()).orElseThrow(
                    () -> new CustomNotFoundException("VinylEntity not found in AuctionEntityMapper"));
            auctionEntity.setVinyl(vinylEntity);
        }

        // Handling the user entity correctly
        if (auction.getSeller() != null && auction.getSeller().getId() != null) {
            UserEntity userEntity = userJPARepo.findById(auction.getSeller().getId()).orElseThrow(
                    () -> new CustomNotFoundException("UserEntity not found in AuctionEntityMapper"));
            auctionEntity.setSeller(userEntity);
        }

        return auctionEntity;
    }

    // Converts AuctionEntity to Auction domain object
    public static Auction fromEntity(AuctionEntity entity) {
        if (entity == null) {
            return null;
        }

        Auction auction = new Auction();
        auction.setId(entity.getId()); // Retrieve the auto-generated ID from the entity
        auction.setTitle(entity.getTitle());
        auction.setDescription(entity.getDescription());
        auction.setStartingPrice(entity.getStartingPrice());
        auction.setCurrentPrice(entity.getCurrentPrice());
        auction.setStartTime(entity.getStartTime());
        auction.setEndTime(entity.getEndTime());
        auction.setAuctionStatus(entity.getAuctionStatus());

        auction.setVinyl(VinylEntityMapper.fromEntity(entity.getVinyl()));

        auction.setSeller(UserEntityMapper.fromEntity(entity.getSeller()));

        return auction;
    }

    public static AuctionDTO toAuctionDTO(AuctionEntity entity) {
        if (entity == null) {
            return null;
        }

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(entity.getId());
        auctionDTO.setTitle(entity.getTitle());
        auctionDTO.setDescription(entity.getDescription());
        auctionDTO.setStartingPrice(entity.getStartingPrice());
        auctionDTO.setCurrentPrice(entity.getCurrentPrice());
        auctionDTO.setStartTime(entity.getStartTime());
        auctionDTO.setEndTime(entity.getEndTime());
        auctionDTO.setVinyl_id(entity.getVinyl().getId());
        auctionDTO.setSeller_id(entity.getSeller().getId());

        return auctionDTO;
    }

    public static AuctionDTO toAuctionDTOSummary(AuctionEntity entity) {
        if (entity == null) {
            return null;
        }

        AuctionDTO auctionDTO = new AuctionDTO();

        auctionDTO.setId(entity.getId());
        auctionDTO.setTitle(entity.getTitle());
        auctionDTO.setCurrentPrice(entity.getCurrentPrice());
        auctionDTO.setEndTime(entity.getEndTime());

        return auctionDTO;
    }

    public static AuctionsDTO toAuctionSummaryDTO(List<AuctionEntity> entity) {
        AuctionsDTO dtos = new AuctionsDTO();
        for (AuctionEntity a : entity) {
            dtos.getAuctions().add(toAuctionDTOSummary(a));
        }

        return dtos;
    }
}