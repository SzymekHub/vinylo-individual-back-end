package s3.individual.vinylo.serviceIMPL;

import java.util.*;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.persistence.ArtistRepo;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.exceptions.CustomNotFoundException;

@Service
@RequiredArgsConstructor
public class ArtistServiceIMPL implements ArtistService {

    private final ArtistRepo artistRepo;

    @Override
    @Transactional
    public Artist saveArtist(Integer id, Artist newArtist) {
        if (id != null) {
            // Check if the artist exists
            Artist existingArtist = artistRepo.getArtistById(id);
            if (existingArtist == null) {
                // Throw an exception if the artist does not exist
                throw new CustomNotFoundException(
                        "Artist with ID " + id + " was not found. A new artist will be created");
            }
            // Update the existing artists bio with new details
            existingArtist.setBio(newArtist.getBio());

            // Save the updated artist to the database
            return artistRepo.saveArtist(existingArtist);

        }
        // if no ID is provided or artist does not exist, create a new one
        return artistRepo.saveArtist(newArtist);
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
