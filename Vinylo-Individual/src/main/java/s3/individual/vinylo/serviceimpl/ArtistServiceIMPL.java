package s3.individual.vinylo.serviceimpl;

import java.util.*;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.persistence.ArtistRepo;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.dtos.ArtistDTO;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.exceptions.DuplicateItemException;

@Service
@RequiredArgsConstructor
public class ArtistServiceIMPL implements ArtistService {

    private final ArtistRepo artistRepo;

    @Override
    @Transactional
    public Artist createArtist(Artist newArtist) {
        try {
            // Check if the artist exists
            Artist existingArtist = artistRepo.getArtistByName(newArtist.getName());

            if (existingArtist != null) {
                // Throw an exception if the artist does not exist
                throw new DuplicateItemException(
                        "An Artist with the same name already exists");
            }
            return artistRepo.saveArtist(newArtist);
        } catch (DuplicateItemException ex) {
            throw ex;
        } catch (Exception e) {
            throw new CustomInternalServerErrorException("Failed to save the Artist. " + e.toString());
        }
    }

    @Override
    @Transactional
    public Artist updateArtist(Integer Id, ArtistDTO artistDTO) {
        try {
            Artist artistToUpdate = artistRepo.getArtistById(Id);

            if (artistToUpdate == null) {
                throw new CustomNotFoundException("Artist with ID: " + Id + " was not found");
            }

            artistToUpdate.setId(artistToUpdate.getId());

            artistToUpdate.setName(artistToUpdate.getName());
            artistToUpdate.setBio(artistToUpdate.getBio());

            return artistRepo.saveArtist(artistToUpdate);
        } catch (CustomNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new CustomInternalServerErrorException("Failed to update the artist. " + e.toString());
        }
    }

    @Override
    public Artist getArtistById(int id) {
        return artistRepo.getArtistById(id);
    }

    @Override
    public List<Artist> getArtists(int page, int size) {

        int offset = page * size;

        return artistRepo.getArtists(offset, size);
    }

    @Override
    public int getTotalArtistsCount() {
        return artistRepo.getTotalArtistsCount();
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
