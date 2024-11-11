package s3.individual.vinylo.domain.mappers;

import java.util.*;

import s3.individual.vinylo.domain.dtos.ArtistDTO;
import s3.individual.vinylo.domain.dtos.ArtistsDTO;
import s3.individual.vinylo.domain.Artist;

public class ArtistMapper {

    private ArtistMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Artist toArtist(ArtistDTO artistDTO) {
        if (artistDTO == null) {
            return null;
        }

        return new Artist(
                artistDTO.getId(),
                artistDTO.getName(),
                artistDTO.getBio());
    }

    public static ArtistDTO toArtistDTO(Artist artist) {
        if (artist == null) {
            return null;
        }

        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setId(artist.getId());
        artistDTO.setName(artist.getName());
        artistDTO.setBio(artist.getBio());
        return artistDTO;
    }

    public static ArtistsDTO toArtistsDTO(List<Artist> artists) {
        ArtistsDTO ad = new ArtistsDTO();
        for (Artist a : artists) {
            ad.getArtists().add(toArtistDTO(a));
        }
        return ad;
    }

    public static List<Artist> tArtists(ArtistsDTO ad) {
        List<Artist> artists = new ArrayList<>();
        for (ArtistDTO a : ad.getArtists()) {
            artists.add(toArtist(a));
        }
        return artists;
    }
}
