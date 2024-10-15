package s3.individual.vinylo.Models.persistence;

import java.util.List;

import s3.individual.vinylo.services.domain.Artist;

public interface ArtistRepo {

    List<Artist> getArtists();

    Artist getArtistByName(String name);

    Artist getArtistById(int id);

    Artist createNewArtist(Artist newArtist);

    boolean deactivateArtistById(int id);

}