package s3.individual.vinylo.domain.mappers;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.entity.VinylCollectionEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;
import s3.individual.vinylo.persistence.jparepository.VinylJPARepo;

@Component // Make this class a Spring-managed bean
public class VinylCollectionEntityMapper {

    private static VinylJPARepo vinylJPARepo;
    private static UserJPARepo userJPARepo;

    public VinylCollectionEntityMapper(VinylJPARepo vinylJPARepo, UserJPARepo userJPARepo) {
        VinylCollectionEntityMapper.vinylJPARepo = vinylJPARepo;
        VinylCollectionEntityMapper.userJPARepo = userJPARepo;

    }

    public static VinylCollectionEntity toEntity(VinylCollection domain) {

        if (domain == null) {
            return null;
        }

        VinylCollectionEntity vinylCollectionEntity = new VinylCollectionEntity();
        // Do not set ID, as it's auto-generated by the database

        if (domain.getUser() != null && domain.getUser().getId() != null) {
            UserEntity userEntity = userJPARepo.findById(domain.getUser().getId()).orElseThrow(
                    () -> new CustomNotFoundException("UserEntity not found in VinylCollectionMapper"));
            vinylCollectionEntity.setUser(userEntity);
        }

        if (domain.getVinyl() != null && domain.getVinyl().getId() != null) {
            VinylEntity vinylEntity = vinylJPARepo.findById(domain.getVinyl().getId()).orElseThrow(
                    () -> new CustomNotFoundException("VinylEntity not found in VinylCollectionMapper"));
            vinylCollectionEntity.setVinyl(vinylEntity);
        }

        return vinylCollectionEntity;
    }

    public static VinylCollection fromEntity(VinylCollectionEntity entity) {
        if (entity == null) {
            return null;
        }
        VinylCollection vinylCollection = new VinylCollection();
        vinylCollection.setId(entity.getId());
        vinylCollection.setUser(UserEntityMapper.fromEntity(entity.getUser()));
        vinylCollection.setVinyl(VinylEntityMapper.fromEntity(entity.getVinyl()));

        return vinylCollection;
    }
}
