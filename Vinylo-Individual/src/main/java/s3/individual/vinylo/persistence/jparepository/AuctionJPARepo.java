package s3.individual.vinylo.persistence.jparepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import s3.individual.vinylo.persistence.entity.AuctionEntity;

public interface AuctionJPARepo extends JpaRepository<AuctionEntity, Integer> {
    boolean existsById(int id);

    Optional<AuctionEntity> findByTitle(String title);

}
