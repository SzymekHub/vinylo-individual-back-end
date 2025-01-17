package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.dtos.ArtistDTO;

public interface ArtistService {

    Artist createArtist(Artist newArtist);

    Artist updateArtist(Integer id, ArtistDTO artistDTO);

    Artist getArtistById(int id);

    List<Artist> getArtists(int page, int size);

    int getTotalArtistsCount();

    Artist getArtistByName(String name);

    boolean deactivateArtistById(int id);

}