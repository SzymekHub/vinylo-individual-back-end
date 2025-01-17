package s3.individual.vinylo.controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import s3.individual.vinylo.exceptions.CustomNotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.domain.mappers.ArtistMapper;
import s3.individual.vinylo.domain.dtos.ArtistDTO;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.domain.Artist;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;
    private final SimpMessagingTemplate messagingTemplate;

    public ArtistController(ArtistService artistService, SimpMessagingTemplate messagingTemplate) {
        this.artistService = artistService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping()
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public Map<String, Object> getArtists(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        List<Artist> artists = artistService.getArtists(page, size);

        // Get the total count of artist records
        int totalCount = artistService.getTotalArtistsCount();
        // Prepare the response with paginated data and metadata
        Map<String, Object> response = new HashMap<>();
        // Accesses the list within ArtistsDTO
        response.put("artists", ArtistMapper.toArtistsDTO(artists).getArtists());
        // Adds totalCount of vinyls to the response
        response.put("totalCount", totalCount);
        // Adds the current page number to response.
        response.put("currentPage", page);
        // Calculates and add total pages.
        response.put("totalPages", (int) Math.ceil((double) totalCount / size));

        return response;
    }

    @GetMapping("{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
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

        artistService.createArtist(newArtist);

        return ResponseEntity.status(HttpStatus.CREATED).body("Artist created successfully");
    }

    @PutMapping("/{id}")
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity<ArtistDTO> replaceArtistBio(@Valid @RequestBody ArtistDTO artistDTO,
            @PathVariable("id") int id) {

        Artist updatedArtist = artistService.updateArtist(id, artistDTO);
        ArtistDTO updateArtistDTO = ArtistMapper.toArtistDTO(updatedArtist);

        messagingTemplate.convertAndSend("/topic/ArtistUpdated" + id, updateArtistDTO);
        return ResponseEntity.ok(updateArtistDTO);
    }

    @DeleteMapping("/{artistId}")
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity<String> deleteArtist(
            @PathVariable("artistId") int artistId) {

        boolean isDeleted = artistService.deactivateArtistById(artistId);
        if (isDeleted) {
            String message = String.format("{\"artistId\": %d}", artistId);
            messagingTemplate.convertAndSend("/topic/artists/" + artistId, message);
            return ResponseEntity.ok("Artist was successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artist was not found.");
        }
    }

}
