package s3.individual.vinylo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.domain.Mappers.VinylMapper;
import s3.individual.vinylo.domain.dtos.VinylDTO;
import s3.individual.vinylo.domain.dtos.VinylsDTO;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.domain.Vinyl;

import java.util.List;

@RestController
@RequestMapping("/vinyls")

public class VinylController {

    @Autowired
    private VinylService vinylService;

    @GetMapping()
    public VinylsDTO getVinyls() {
        List<Vinyl> vinyls = vinylService.getVinyls();

        VinylsDTO result = VinylMapper.toVinylsDTO(vinyls);
        return result;
    }

    // I changed return type to ResponseEntity<?> to handle HTTP responses.
    // This allows the method to return both a ResponseEntity<VinylDTO> when a valid
    // vinyl record is found,
    // and a ResponseEntity<String> when an error occurs (e.g., vinyl not found).
    @GetMapping("{id}")
    public ResponseEntity<?> getVinyl(@PathVariable("id") int id) {

        Vinyl v = vinylService.getVinylById(id);

        if (v == null) {
            // If the vinyl record is not found, throw this specific exception
            throw new CustomNotFoundException("Vinyl record not found");
        }

        VinylDTO vd = VinylMapper.toVinylDTO(v);

        return ResponseEntity.ok(vd);
    }

    @PostMapping()
    public VinylDTO createNewVinyl(@RequestBody @Valid VinylDTO newVinylDTO) {
        Vinyl newVinyl = VinylMapper.toVinyl(newVinylDTO);
        Vinyl cretedVinyl = vinylService.createNewVinyl(newVinyl);
        return VinylMapper.toVinylDTO(cretedVinyl);
    }

    @PutMapping("{id}")
    public VinylDTO replaceVinyl(@RequestBody VinylDTO newVinylDTO, @PathVariable("id") int id) {
        Vinyl newVinyl = VinylMapper.toVinyl(newVinylDTO);
        Vinyl replacedVinyl = vinylService.replaceVinyl(id, newVinyl);
        return VinylMapper.toVinylDTO(replacedVinyl);
    }

    // I changed return type to ResponseEntity<String> to handle HTTP responses.
    @DeleteMapping("{id}")
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
