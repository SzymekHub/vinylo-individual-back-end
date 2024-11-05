package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Artist;

public interface ArtistService {

    Artist createNewArtist(Artist newArtist);

    Artist geArtistById(int id);

    List<Artist> getArtists();

    Artist getArtistByName(String name);

    boolean deactivateArtistById(int id);

}