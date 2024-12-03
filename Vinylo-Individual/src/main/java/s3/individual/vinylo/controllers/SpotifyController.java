package s3.individual.vinylo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import s3.individual.vinylo.services.SpotifyService;

@RestController
@RequestMapping("/spotify-embed")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<String> getEmbedUrl(@RequestParam String albumId) {
        String embedUrl = spotifyService.getAlbumEmbedUrl(albumId);
        if (embedUrl != null) {
            return ResponseEntity.ok(embedUrl);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Album not found");
        }
    }
}
