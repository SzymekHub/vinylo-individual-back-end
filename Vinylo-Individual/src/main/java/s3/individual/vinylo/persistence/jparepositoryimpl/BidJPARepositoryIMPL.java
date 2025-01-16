package s3.individual.vinylo.persistence.jparepositoryIMPL;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import s3.individual.vinylo.domain.Bid;
import s3.individual.vinylo.domain.mappers.BidEntityMapper;
import s3.individual.vinylo.persistence.BidRepo;
import s3.individual.vinylo.persistence.entity.AuctionEntity;
import s3.individual.vinylo.persistence.entity.BidEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.BidJPARepo;

@Repository
public class BidJPARepositoryIMPL implements BidRepo {

    private final BidJPARepo bidJPARepo;

    @PersistenceContext
    private EntityManager entityManager;

    public BidJPARepositoryIMPL(BidJPARepo bidJPARepo) {
        this.bidJPARepo = bidJPARepo;
    }

    @Override
    @Transactional
    public Bid saveBid(Bid bid) {

        BidEntity entity = BidEntityMapper.toEntity(bid);
        AuctionEntity managedAuction = entityManager.merge(entity.getAuction());
        UserEntity managedUser = entityManager.merge(entity.getUser());
        entity.setAuction(managedAuction);
        entity.setUser(managedUser);

        // Check if id is null or 0, then save if it's not then update
        if (entity.getId() == 0) {
            BidEntity savedEntity = bidJPARepo.save(entity);
            return BidEntityMapper.fromEntity(savedEntity);
        }
        BidEntity mergedBidEntity = entityManager.merge(entity);
        return BidEntityMapper.fromEntity(mergedBidEntity);
    }

    @Override
    public List<Bid> getBidsByAuctionId(int auctionId) {
        return bidJPARepo.findByAuctionIdOrderByBidAmountDesc(auctionId).stream()
                .map(BidEntityMapper::fromEntity)
                .collect(Collectors.toList());
    }
}