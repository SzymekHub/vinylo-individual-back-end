package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.Artist;

public interface ArtistRepo {

    List<Artist> getArtists(int offset, int limit);

    int getTotalArtistsCount();

    Artist getArtistByName(String name);

    Artist getArtistById(int id);

    Artist saveArtist(Artist newArtist);

    boolean deactivateArtistById(int id);

}