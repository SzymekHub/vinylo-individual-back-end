package s3.individual.vinylo.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import s3.individual.vinylo.Exceptions.CustomNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import s3.individual.vinylo.Models.Mappers.ArtistMapper;
import s3.individual.vinylo.Models.dtos.ArtistDTO;
import s3.individual.vinylo.Models.dtos.ArtistsDTO;
import s3.individual.vinylo.Models.services.ArtistService;
import s3.individual.vinylo.serviceIMPL.domain.Artist;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;
    
    @GetMapping()
    public ArtistsDTO getArtists() {
        List<Artist> artists = artistService.getArtists();

        ArtistsDTO result = ArtistMapper.toArtistsDTO(artists);

        return result;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getArtistById(@PathVariable("id") int id) 
    {
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
