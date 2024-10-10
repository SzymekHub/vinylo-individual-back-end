package s3.individual.vinylo.Models.Mappers;

import java.util.*;

import s3.individual.vinylo.Models.dtos.VinylDTO;
import s3.individual.vinylo.Models.dtos.VinylsDTO;
import s3.individual.vinylo.services.domain.Vinyl;

public class VinylMapper {

    public static Vinyl toVinyl(VinylDTO vinylDTO) {
        if (vinylDTO == null) {
            return null;
        }

        return new Vinyl(
                vinylDTO.getId(),
                vinylDTO.getVinylType(),
                vinylDTO.getTitle(),
                vinylDTO.getDescription(),
                vinylDTO.getIsReleased(),
                vinylDTO.getArtistName()
        );
    }

    public static VinylDTO toVinylDTO(Vinyl vinyl) {

        if (vinyl == null) {
            return null;
        }

        VinylDTO vinylDTO = new VinylDTO();

        vinylDTO.setId(vinyl.getId());
        vinylDTO.setVinylType(vinyl.getvinylType());
        vinylDTO.setTitle(vinyl.getTitle());
        vinylDTO.setDescription(vinyl.getDescription());
        vinylDTO.setIsReleased(vinyl.getisReleased());
        vinylDTO.setArtistName(vinyl.getArtistName());

        return vinylDTO;
        
    }

    public static VinylsDTO toVinylsDTO(List<Vinyl> vinyls) {
        VinylsDTO vinylsDTO = new VinylsDTO();
        for (Vinyl v : vinyls) {
            vinylsDTO.vinyls.add(toVinylDTO(v));
        }
        return vinylsDTO;
    }

    public static List<Vinyl> toVinyls(VinylsDTO vinylsDTO) {
        List<Vinyl> vinyls = new ArrayList<>();
        for (VinylDTO v : vinylsDTO.vinyls) {
            vinyls.add(toVinyl(v));
        }
        return vinyls;
    }

}
