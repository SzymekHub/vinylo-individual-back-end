package s3.individual.vinylo.domain.mappers;

import java.util.*;
import org.springframework.stereotype.Component;
import s3.individual.vinylo.domain.dtos.VinylDTO;
import s3.individual.vinylo.domain.dtos.VinylsDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;

@Component // Make this class a Spring-managed bean
public class VinylMapper {

    private static ArtistService artistService;

    // Constructor-based injection for ArtistService
    public VinylMapper(ArtistService artistService) {
        VinylMapper.artistService = artistService; // Set the ArtistService for static methods
    }

    // private VinylMapper() {
    // throw new UnsupportedOperationException("Utility class");
    // }

    public static Vinyl toVinyl(VinylDTO vinylDTO) {
        if (vinylDTO == null) {
            return null;
        }

        // Ensure that artistService is not null before accessing
        if (artistService == null) {
            throw new CustomNotFoundException("ArtistService not set in VinylMapper");
        }

        // Retrieve the artist if not already set in the DTO
        Artist artist = artistService.getArtistById(vinylDTO.getArtist_id());

        return new Vinyl(
                vinylDTO.getId(),
                VinylTypeEnum.valueOf(vinylDTO.getVinylType()),
                SpeedEnum.valueOf(vinylDTO.getSpeed()),
                vinylDTO.getTitle(),
                vinylDTO.getDescription(),
                StateEnum.valueOf(vinylDTO.getState()),
                VinylColorEnum.valueOf(vinylDTO.getColor()),
                vinylDTO.getIsReleased(),
                artist);
    }

    public static VinylDTO toVinylDTO(Vinyl vinyl) {

        if (vinyl == null) {
            return null;
        }

        VinylDTO vinylDTO = new VinylDTO();

        vinylDTO.setId(vinyl.getId());
        vinylDTO.setVinylType(vinyl.getVinylType().name());
        vinylDTO.setSpeed(vinyl.getSpeed().name());
        vinylDTO.setTitle(vinyl.getTitle());
        vinylDTO.setDescription(vinyl.getDescription());
        vinylDTO.setState(vinyl.getState().name());
        vinylDTO.setColor(vinyl.getColor().name());
        vinylDTO.setIsReleased(vinyl.getIsReleased());
        vinylDTO.setArtist_id(vinyl.getArtist().getId());

        return vinylDTO;
    }

    public static VinylDTO toVinylDTOSummary(Vinyl vinyl) {

        if (vinyl == null) {
            return null;
        }

        VinylDTO vinylDTO = new VinylDTO();

        vinylDTO.setId(vinyl.getId());
        vinylDTO.setVinylType(vinyl.getVinylType().name());
        vinylDTO.setTitle(vinyl.getTitle());
        vinylDTO.setState(vinyl.getState().name());

        return vinylDTO;
    }

    public static VinylsDTO toVinylSummaryDTO(List<Vinyl> vinyl) {
        VinylsDTO dtos = new VinylsDTO();
        for (Vinyl v : vinyl) {
            dtos.getVinyls().add(toVinylDTOSummary(v));
        }

        return dtos;
    }

    public static VinylsDTO toVinylsDTO(List<Vinyl> vinyls) {
        VinylsDTO vinylsDTO = new VinylsDTO();
        for (Vinyl v : vinyls) {
            vinylsDTO.getVinyls().add(toVinylDTO(v));
        }
        return vinylsDTO;
    }

    public static List<Vinyl> toVinyls(VinylsDTO vinylsDTO) {
        List<Vinyl> vinyls = new ArrayList<>();
        for (VinylDTO v : vinylsDTO.getVinyls()) {
            vinyls.add(toVinyl(v));
        }
        return vinyls;
    }
}
