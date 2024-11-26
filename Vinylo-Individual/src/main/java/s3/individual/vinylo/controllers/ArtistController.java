package s3.individual.vinylo.controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.domain.mappers.ArtistMapper;
import s3.individual.vinylo.domain.dtos.ArtistDTO;
import s3.individual.vinylo.domain.dtos.ArtistsDTO;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.domain.Artist;
import org.springframework.web.bind.annotation.PutMapping;

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
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable("id") int id) {
        Artist a = artistService.getArtistById(id);

        if (a == null) {
            throw new CustomNotFoundException("Artist not found");
        }

        ArtistDTO ad = ArtistMapper.toArtistDTO(a);

        return ResponseEntity.ok(ad);
    }

    @PostMapping()
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity<?> createArtist(@Valid @RequestBody ArtistDTO newArtistDTO) {
        Artist newArtist = ArtistMapper.toArtist(newArtistDTO);

        artistService.saveArtist(null, newArtist);

        return ResponseEntity.status(HttpStatus.CREATED).body("Artist created successfully");
    }

    @PutMapping("{id}")
    @RolesAllowed({ "ADMIN" })
    public ArtistDTO replaceArtistBio(@RequestBody ArtistDTO newArtistDTO, @PathVariable("id") int id) {
        Artist newArtist = ArtistMapper.toArtist(newArtistDTO);
        Artist replaceArtist = artistService.saveArtist(id, newArtist);
        return ArtistMapper.toArtistDTO(replaceArtist);
    }

}
