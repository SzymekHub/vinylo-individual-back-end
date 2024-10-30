package s3.individual.vinylo.serviceIMPL;

import java.util.*;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.ArtistRepo;
import s3.individual.vinylo.Models.services.ArtistService;
import s3.individual.vinylo.serviceIMPL.domain.Artist;

@Service
public class ArtistServiceIMPL implements ArtistService {

    private final ArtistRepo artistRepo;

    public ArtistServiceIMPL(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    @Override
    public Artist createNewArtist(Artist newArtist) {

        return artistRepo.createNewArtist(newArtist);
    }
    
    @Override
    public Artist geArtistById(int id) {
        return artistRepo.getArtistById(id);
    }

    @Override
    public List<Artist> getArtists() {
        return artistRepo.getArtists();
    }

    @Override
    public Artist getArtistByName(String name) {
        return artistRepo.getArtistByName(name);
    }

    @Override
    public boolean deactivateArtistById(int id) {
        return artistRepo.deactivateArtistById(id);
    }
}
