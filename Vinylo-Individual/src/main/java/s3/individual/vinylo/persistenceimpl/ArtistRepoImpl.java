// package s3.individual.vinylo.persistenceIMPL;

// import java.util.*;

// import org.springframework.stereotype.Repository;

// import s3.individual.vinylo.persistence.ArtistRepo;
// import s3.individual.vinylo.domain.Artist;

// @Repository
// public class ArtistRepoImpl implements ArtistRepo {
// private final List<Artist> allArtists;

// public ArtistRepoImpl() {
// allArtists = CreateSomeArtists();
// }

// private List<Artist> CreateSomeArtists() {
// List<Artist> artists = new ArrayList<>();
// artists.add(new Artist(0, "PLAYBOI CARTI", "syrup"));
// artists.add(new Artist(1, "The Beatles", "Yeah yeah"));
// artists.add(new Artist(2, "$uicideBoy$", "G59 till I die"));
// artists.add(new Artist(3, "Mac Miller", "MOST DOPE that's forever"));
// artists.add(new Artist(4, "Clairo", "Charm"));
// artists.add(new Artist(5, "BONES", "Sesh"));
// artists.add(new Artist(6, "Ken Carson", "Huh huh huh"));
// artists.add(new Artist(7, "Ian", "Come on"));
// artists.add(new Artist(8, "Led Zeppelin", "HEY HEY HEY MAMA"));
// artists.add(new Artist(9, "Fat Nick", "2 HOT 4 U"));

// return artists;
// }

// @Override
// public List<Artist> getArtists() {
// return new ArrayList<>(allArtists);
// }

// @Override
// public Artist getArtistByName(String name) {

// for (Artist a : allArtists) {
// if (a.name == (name)) {
// return a;
// }
// }
// return null;
// }

// @Override
// public Artist getArtistById(int id) {
// for (Artist a : allArtists) {
// if (a.id == id) {
// return a;
// }
// }
// return null;
// }

// @Override
// public Artist createNewArtist(Artist newArtist) {
// allArtists.add(newArtist);
// return newArtist;
// }

// @Override
// public boolean deactivateArtistById(int id) {
// return allArtists.removeIf(a -> a.id == (id));
// }
// }
