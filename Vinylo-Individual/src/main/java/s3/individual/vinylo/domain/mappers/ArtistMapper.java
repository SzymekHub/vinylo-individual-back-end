package s3.individual.vinylo.domain.mappers;

import java.util.*;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.dtos.ArtistDTO;
import s3.individual.vinylo.domain.dtos.ArtistsDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.domain.Artist;

@Component
public class ArtistMapper {

    public static Artist toArtist(ArtistDTO artistDTO) {

        if (artistDTO == null) {
            throw new CustomNotFoundException("ArtistDTO not set in ArtistMapper");
        }

        return new Artist(
                artistDTO.getId(),
                artistDTO.getName(),
                artistDTO.getBio());
    }

    public static ArtistDTO toArtistDTO(Artist artist) {
        if (artist == null) {
            throw new CustomNotFoundException("Artist not set in ArtistMapper");
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
