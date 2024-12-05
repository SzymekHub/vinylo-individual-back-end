package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.persistence.entity.VinylEntity;

public interface VinylCollectionRepo {

    VinylCollection save(VinylCollection vinylCollection);

    List<VinylCollection> findAll();

    VinylCollection findByUserAndVinyl(int userId, int vinylId);

    VinylCollection findById(int id);

    List<VinylEntity> findByUserId(int userId);

    VinylEntity findByUserIdAndVinylId(int userId, int vinylId);

    boolean deleteById(int id);
}
