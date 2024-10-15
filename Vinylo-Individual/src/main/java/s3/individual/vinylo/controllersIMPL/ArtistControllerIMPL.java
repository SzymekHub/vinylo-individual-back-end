package s3.individual.vinylo.controllersIMPL;

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
import s3.individual.vinylo.Models.controllers.ArtistController;
import s3.individual.vinylo.Models.dtos.ArtistDTO;
import s3.individual.vinylo.Models.dtos.ArtistsDTO;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.services.domain.Artist;

@RestController
@RequestMapping("/artist")
public class ArtistControllerIMPL implements ArtistController {

    @Autowired
    private ArtistService artistService;
    
    @Override
    @GetMapping()
    public ArtistsDTO getArtists() {
        List<Artist> artists = artistService.getArtists();

        ArtistsDTO result = ArtistMapper.toArtistsDTO(artists);

        return result;
    }

    @Override
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

    @Override
    @PostMapping()
    public ArtistDTO createArtist(@RequestBody @Valid ArtistDTO newArtist) {

        Artist newArtistEntity = ArtistMapper.toArtist(newArtist);
        Artist createArtist = artistService.createNewArtist(newArtistEntity);
        return ArtistMapper.toArtistDTO(createArtist);
    }

}
