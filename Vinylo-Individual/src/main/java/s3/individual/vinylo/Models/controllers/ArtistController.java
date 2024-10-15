package s3.individual.vinylo.Models.controllers;

import org.springframework.http.ResponseEntity;
import s3.individual.vinylo.Models.dtos.ArtistDTO;
import s3.individual.vinylo.Models.dtos.ArtistsDTO;

public interface ArtistController {

    ArtistsDTO getArtists();

    ResponseEntity<?> getArtistById(int id);

    ArtistDTO createArtist(ArtistDTO newArtist);

}