package s3.individual.vinylo.controllers;

import java.util.*;

import org.springframework.http.ResponseEntity;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import s3.individual.vinylo.domain.Mappers.ArtistMapper;
import s3.individual.vinylo.domain.dtos.ArtistDTO;
import s3.individual.vinylo.domain.dtos.ArtistsDTO;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.domain.Artist;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public ArtistsDTO getArtists() {
        List<Artist> artists = artistService.getArtists();

        return ArtistMapper.toArtistsDTO(artists);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getArtistById(@PathVariable("id") int id) {
        Artist a = artistService.geArtistById(id);

        if (a == null) {
            throw new CustomNotFoundException("Artist record not found");
        }

        ArtistDTO ad = ArtistMapper.toArtistDTO(a);

        return ResponseEntity.ok(ad);
    }

    @PostMapping()
    public ArtistDTO createArtist(@RequestBody @Valid ArtistDTO newArtist) {

        Artist newArtistEntity = ArtistMapper.toArtist(newArtist);
        Artist createArtist = artistService.createNewArtist(newArtistEntity);
        return ArtistMapper.toArtistDTO(createArtist);
    }

}
