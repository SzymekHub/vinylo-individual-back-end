package s3.individual.vinylo.persistence.jparepositoryIMPL;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.mappers.AuctionEntityMapper;
import s3.individual.vinylo.persistence.AuctionRepo;
import s3.individual.vinylo.persistence.entity.AuctionEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.jparepository.AuctionJPARepo;

@Repository
public class AuctionJPARepositoryIMPL implements AuctionRepo {

    private final AuctionJPARepo auctionJPARepo;

    @PersistenceContext
    private EntityManager entityManager;

    public AuctionJPARepositoryIMPL(AuctionJPARepo auctionJPARepo) {
        this.auctionJPARepo = auctionJPARepo;
    }

    @Override
    public List<Auction> getAuctions(int offset, int limit) {

        String query = "SELECT a FROM AuctionEntity a ORDER BY a.id";

        return entityManager.createQuery(query, AuctionEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList()
                .stream()
                .map(AuctionEntityMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalAuctionsCount() {
        return auctionJPARepo.getTotalAuctionsCount();
    }

    @Override
    public Auction findByVinylAndTitle(int vinylId, String Title) {
        return auctionJPARepo.findByVinylAndTitle(vinylId, Title)
                .map(AuctionEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public Auction getAuctionByTitle(String title) {
        return auctionJPARepo.findByTitle(title)
                .map(AuctionEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public Auction getAuctionById(int id) {
        return auctionJPARepo.findById(id)
                .map(AuctionEntityMapper::fromEntity).get();
    }

    @Override
    @Transactional
    public Auction saveAuction(Auction auction) {

        AuctionEntity entity = AuctionEntityMapper.toEntity(auction);
        VinylEntity managedVinyl = entityManager.merge(entity.getVinyl());
        UserEntity managedSeller = entityManager.merge(entity.getSeller());
        entity.setVinyl(managedVinyl);
        entity.setSeller(managedSeller);

        AuctionEntity savedEntity = auctionJPARepo.save(entity);
        return AuctionEntityMapper.fromEntity(savedEntity);
    }

    @Override
    public boolean deativateAuctionById(int id) {
        if (auctionJPARepo.existsById(id)) {
            auctionJPARepo.deleteById(id);
            return true;
        }
        return false;
    }
}
