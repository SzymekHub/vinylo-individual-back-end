package s3.individual.vinylo.persistence.jparepositoryimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.domain.mappers.VinylCollectionEntityMapper;
import s3.individual.vinylo.persistence.VinylCollectionRepo;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.entity.VinylCollectionEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.jparepository.VinylCollectionJPARepo;

@Repository
public class VinylCollectionJPARepoIMPL implements VinylCollectionRepo {

    private final VinylCollectionJPARepo vinylCollectionJPARepo;

    @PersistenceContext
    private EntityManager entityManager;

    public VinylCollectionJPARepoIMPL(VinylCollectionJPARepo vinylCollectionJPARepo) {
        this.vinylCollectionJPARepo = vinylCollectionJPARepo;
    }

    @Override
    @Transactional
    public VinylCollection save(VinylCollection vinylCollection) {
        VinylCollectionEntity entity = VinylCollectionEntityMapper.toEntity(vinylCollection);
        UserEntity managedEntity = entityManager.merge(entity.getUser());
        VinylEntity manVinylEntity = entityManager.merge(entity.getVinyl());
        entity.setUser(managedEntity);
        entity.setVinyl(manVinylEntity);
        VinylCollectionEntity savedVinylCollectionEntity = vinylCollectionJPARepo.save(entity);
        return VinylCollectionEntityMapper.fromEntity(savedVinylCollectionEntity);
    }

    @Override
    public List<VinylCollection> findAll() {
        return vinylCollectionJPARepo.findAll().stream()
                .map(VinylCollectionEntityMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VinylCollection findByUserAndVinyl(int userId, int vinylId) {
        return vinylCollectionJPARepo.findByUserAndVinyl(userId, vinylId)
                .map(VinylCollectionEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public VinylCollection findById(int id) {
        return vinylCollectionJPARepo.findById(id)
                .map(VinylCollectionEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public List<VinylEntity> findByUserId(int userId) {
        return vinylCollectionJPARepo.findByUserId(userId);
    }

    @Override
    public VinylEntity findByUserIdAndVinylId(int userId, int vinylId) {
        return vinylCollectionJPARepo.findByUserIdAndVinylId(userId, vinylId).orElse(null);
    }

    @Override
    public boolean deleteByVinylIdAndUserId(int vinylId, int userId) {
        try {
            vinylCollectionJPARepo.deleteByVinylIdAndUserId(vinylId, userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
