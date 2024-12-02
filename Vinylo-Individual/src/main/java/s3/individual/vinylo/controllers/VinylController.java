package s3.individual.vinylo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import s3.individual.vinylo.domain.mappers.VinylMapper;
import s3.individual.vinylo.domain.dtos.VinylDTO;
import s3.individual.vinylo.domain.dtos.VinylsDTO;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vinyls")

public class VinylController {

    private final VinylService vinylService;
    private final ArtistService artistService;

    public VinylController(VinylService vinylService, ArtistService artistService) {
        this.vinylService = vinylService;
        this.artistService = artistService;
    }

    @GetMapping("/enums")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public Map<String, List<String>> getVinylEnums() {
        Map<String, List<String>> enums = new HashMap<>();
        enums.put("vinylTypes", Arrays.stream(VinylTypeEnum.values()).map(Enum::name).collect(Collectors.toList()));
        enums.put("speeds", Arrays.stream(SpeedEnum.values()).map(Enum::name).collect(Collectors.toList()));
        enums.put("states", Arrays.stream(StateEnum.values()).map(Enum::name).collect(Collectors.toList()));
        enums.put("colors", Arrays.stream(VinylColorEnum.values()).map(Enum::name).collect(Collectors.toList()));
        return enums;
    }

    @GetMapping()
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public VinylsDTO getVinyls() {
        List<Vinyl> vinyls = vinylService.getVinyls();

        return VinylMapper.toVinylSummaryDTO(vinyls);
    }

    // I changed return type to ResponseEntity<?> to handle HTTP responses.
    // This allows the method to return both a ResponseEntity<VinylDTO> when a valid
    // vinyl record is found,
    // and a ResponseEntity<String> when an error occurs (e.g., vinyl not found).
    @GetMapping("/{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<VinylDTO> getVinyl(@PathVariable("id") int id) {

        Vinyl v = vinylService.getVinylById(id);

        if (v == null) {
            throw new CustomNotFoundException("Vinyl record not found");
            // If the vinyl record is not found, throw this specific exception
        }

        VinylDTO vd = VinylMapper.toVinylDTO(v);

        return ResponseEntity.ok(vd);
    }

    @PostMapping
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity<?> addVinyl(@Valid @RequestBody VinylDTO vinylDTO) {
        Artist artist = artistService.getArtistById(vinylDTO.getArtist_id());

        // If the artist is not found, return an error response
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Artist with ID " + vinylDTO.getArtist_id() + " not found.");
        }

        // Create the Vinyl object from the DTO
        Vinyl vinyl = VinylMapper.toVinyl(vinylDTO);

        // Set the existing artist in the Vinyl object
        vinyl.setArtist(artist);

        // Save the Vinyl object (without saving the Artist)
        vinylService.saveVinyl(null, vinyl);

        return ResponseEntity.status(HttpStatus.CREATED).body("Vinyl created successfully");
    }

    @PutMapping("/{id}")
    @RolesAllowed({ "ADMIN" })
    public VinylDTO replaceVinyl(@RequestBody VinylDTO newVinylDTO, @PathVariable("id") int id) {
        Vinyl newVinyl = VinylMapper.toVinyl(newVinylDTO);
        Vinyl replacedVinyl = vinylService.saveVinyl(id, newVinyl); // Correctly passing both id and newVinyl
        return VinylMapper.toVinylDTO(replacedVinyl);
    }

    // I changed return type to ResponseEntity<String> to handle HTTP responses.
    @DeleteMapping("/{id}")
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity<String> deleteVinyl(@PathVariable("id") int id) {
        boolean isDeleted = vinylService.deleteVinylById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Vinyl record with id " + id + " was successfully deleted.");
        } else {
            // I used ResponseEntity.status(HttpStatus.NOT_FOUND).body(...) to return a 404
            // status and a message if the vinyl record was not found.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vinyl record with id " + id + " was not found.");
        }
    }
}
