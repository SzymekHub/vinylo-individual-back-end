package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.persistence.entity.VinylEntity;

public interface VinylCollectionService {
    VinylCollection addVinylToCollection(Integer id, VinylCollection vinylCollection);

    List<VinylCollection> getAllCollections();

    VinylCollection getCollectionById(int id);

    List<VinylEntity> getCollectionByUserId(int userId);

    VinylEntity getByUserIdAndVinylId(int userId, int vinylId);

    boolean deleteCollection(int id);
}
