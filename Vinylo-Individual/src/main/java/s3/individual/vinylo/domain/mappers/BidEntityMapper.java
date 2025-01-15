package s3.individual.vinylo.domain.mappers;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.Bid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.entity.AuctionEntity;
import s3.individual.vinylo.persistence.entity.BidEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.AuctionJPARepo;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;

@Component
public class BidEntityMapper {

    private static UserJPARepo userJPARepo;
    private static AuctionJPARepo auctionJPARepo;

    public BidEntityMapper(UserJPARepo userJPARepo, AuctionJPARepo auctionJPARepo) {
        BidEntityMapper.userJPARepo = userJPARepo;
        BidEntityMapper.auctionJPARepo = auctionJPARepo;
    }

    public static BidEntity toEntity(Bid bid) {

        if (bid == null) {
            return null;
        }

        BidEntity bidEntity = new BidEntity();
        if (bid.getId() != null) {
            bidEntity.setId(bid.getId());
        }

        bidEntity.setBidAmount(bid.getBidAmount());
        bidEntity.setBidTime(bid.getBidTime());

        // Handling the user entity correctly
        if (bid.getUserId() != null) {
            UserEntity userEntity = userJPARepo.findById(bid.getUserId()).orElseThrow(
                    () -> new CustomNotFoundException("UserEntity not found in BidEntityMapper"));
            bidEntity.setUser(userEntity);
        }

        // Handling the auction entity correctly
        if (bid.getAuctionId() != null) {
            AuctionEntity auctionEntity = auctionJPARepo.findById(bid.getAuctionId()).orElseThrow(
                    () -> new CustomNotFoundException("AuctionEntity not found in BidEntityMapper"));
            bidEntity.setAuction(auctionEntity);
        }

        return bidEntity;
    }

    public static Bid fromEntity(BidEntity entity) {
        if (entity == null) {
            return null;
        }

        return Bid.builder()
                .id(entity.getId())
                .auctionId(entity.getAuction().getId())
                .userId(entity.getUser().getId())
                .bidAmount(entity.getBidAmount())
                .bidTime(entity.getBidTime())
                .build();

    }

}