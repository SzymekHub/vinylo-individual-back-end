package s3.individual.vinylo.domain.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.domain.dtos.VinylCollectionDTO;
import s3.individual.vinylo.domain.dtos.VinylCollectionsDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.services.VinylService;

@Component // Make this class a Spring-managed bean
public class VinylCollectionMapper {

    private static VinylService vinylService;
    private static UserService userService;

    public VinylCollectionMapper(VinylService vinylService, UserService userService) {
        VinylCollectionMapper.vinylService = vinylService;
        VinylCollectionMapper.userService = userService;
    }

    public static VinylCollection fromDTO(VinylCollectionDTO dto) {
        if (dto == null) {
            return null;
        }

        // Ensure that userService is not null before accessing
        if (userService == null) {
            throw new CustomNotFoundException("UserService not set in VinylCollectionMapper");
        }

        if (vinylService == null) {
            throw new CustomNotFoundException("VinylService not set in VinylCollectionMapper");
        }

        // Retrieve the user if not already set in the DTO
        User user = userService.getUserById(dto.getUser_id());

        Vinyl vinyl = vinylService.getVinylById(dto.getVinyl_id());

        return new VinylCollection(
                dto.getId(),
                user,
                vinyl);
    }

    public static VinylCollectionDTO toDTO(VinylCollection domain) {

        if (domain == null) {
            return null;
        }

        VinylCollectionDTO vinylCollectionDTO = new VinylCollectionDTO();

        vinylCollectionDTO.setId(domain.getId());
        vinylCollectionDTO.setUser_id(domain.getUser().getId());
        vinylCollectionDTO.setVinyl_id(domain.getVinyl().getId());

        return vinylCollectionDTO;
    }

    public static VinylCollectionDTO toVinylCollectionDTOSummary(VinylCollection vinylCollection) {

        if (vinylCollection == null) {
            return null;
        }

        VinylCollectionDTO vinylCollectionDTO = new VinylCollectionDTO();
        vinylCollectionDTO.setId(vinylCollection.getId());
        vinylCollectionDTO.setUser_id(vinylCollection.getUser().getId());
        vinylCollectionDTO.setVinyl_id(vinylCollection.getVinyl().getId());

        return vinylCollectionDTO;

    }

    public static VinylCollectionsDTO toCollectionDTOSummary(List<VinylCollection> collections) {
        VinylCollectionsDTO collectionsDTO = new VinylCollectionsDTO();
        for (VinylCollection vc : collections) {
            collectionsDTO.getCollections().add(toVinylCollectionDTOSummary(vc));
        }
        return collectionsDTO;
    }

    public static VinylCollectionsDTO toCollectionsDTO(List<VinylCollection> collections) {
        VinylCollectionsDTO collectionsDTO = new VinylCollectionsDTO();
        for (VinylCollection vc : collections) {
            collectionsDTO.getCollections().add(toDTO(vc));
        }
        return collectionsDTO;
    }

    public static List<VinylCollection> toVinylCollections(VinylCollectionsDTO dtos) {
        List<VinylCollection> collections = new ArrayList<>();
        for (VinylCollectionDTO vc : dtos.getCollections()) {
            collections.add(fromDTO(vc));
        }
        return collections;
    }

}
