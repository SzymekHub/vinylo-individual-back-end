package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Artist;

public interface ArtistService {

    Artist saveArtist(Integer id, Artist newArtist);

    Artist getArtistById(int id);

    List<Artist> getArtists();

    Artist getArtistByName(String name);

    boolean deactivateArtistById(int id);

}