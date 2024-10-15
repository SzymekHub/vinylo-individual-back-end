package s3.individual.vinylo.services;

import java.util.*;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.ArtistRepo;
import s3.individual.vinylo.services.domain.Artist;

@Service
public class ArtistService {

    private final ArtistRepo artistRepo;

    public ArtistService(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    public Artist createNewArtist(Artist newArtist) {

        return artistRepo.createNewArtist(newArtist);
    }
    
    public Artist geArtistById(int id) {
        return artistRepo.getArtistById(id);
    }

    public List<Artist> getArtists() {
        return artistRepo.getArtists();
    }

    public Artist getArtistByName(String name) {
        return artistRepo.getArtistByName(name);
    }

    public boolean deactivateArtistById(int id) {
        return artistRepo.deactivateArtistById(id);
    }
}
