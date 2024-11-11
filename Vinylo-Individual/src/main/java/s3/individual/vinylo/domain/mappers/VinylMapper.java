package s3.individual.vinylo.domain.mappers;

import java.util.*;

import s3.individual.vinylo.domain.dtos.VinylDTO;
import s3.individual.vinylo.domain.dtos.VinylsDTO;
import s3.individual.vinylo.domain.Vinyl;

public class VinylMapper {

    private VinylMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

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
                ArtistMapper.toArtist(vinylDTO.getArtist()));
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
        vinylDTO.setArtist(ArtistMapper.toArtistDTO(vinyl.getArtist()));

        return vinylDTO;

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
