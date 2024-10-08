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
import s3.individual.vinylo.services.domain.Vinyl;
import s3.individual.vinylo.controllers.Exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.dtos.VinylDTO;
import s3.individual.vinylo.persistence.dtos.VinylsDTO;
import s3.individual.vinylo.services.VinylService;

import java.util.ArrayList;

@RestController
@RequestMapping("/vinyls")

public class VinylController {

    @Autowired
    private VinylService vinylService;

    @GetMapping()
    public VinylsDTO getVinyls()
    {
        ArrayList<Vinyl> vinyls = vinylService.getVinyls();

        VinylsDTO result = new VinylsDTO();

        for (Vinyl v: vinyls)
        {
            VinylDTO vd = new VinylDTO();
            vd.aristName = v.getAristName();
            vd.isReleased = v.getisReleased();
            vd.description = v.getDescription();
            vd.name = v.getName();
            vd.id = v.getId();
            vd.vinylType = v.getvinylType();
            result.vinyls.add(vd);
        }

        return result;
    }

    //I changed return type to ResponseEntity<?> to handle HTTP responses.
    //This allows the method to return both a ResponseEntity<VinylDTO> when a valid vinyl record is found, 
    // and a ResponseEntity<String> when an error occurs (e.g., vinyl not found).
    @GetMapping("{id}")
    public ResponseEntity<?> getVinyl(@PathVariable("id") int id) {

        Vinyl v = vinylService.getVinylById(id);

        if (v == null) {
            // If the vinyl record is not found, throw this specific exception
            throw new CustomNotFoundException("Vinyl record not found");
        }
       
        VinylDTO vd = new VinylDTO();
        vd.aristName = v.getAristName();
        vd.isReleased = v.getisReleased();
        vd.description = v.getDescription();
        vd.name = v.getName();
        vd.vinylType = v.getvinylType();
        vd.id = v.getId();

        return ResponseEntity.ok(vd);
    }

    @PostMapping()
    public Vinyl createNewVinyl(@RequestBody @Valid Vinyl newVinyl) {
        return vinylService.createNewVinyl(newVinyl);
    }

    @PutMapping("{id}")
    public Vinyl replaceVinyl(@RequestBody Vinyl newVinyl, @PathVariable("id") int id) {

        return vinylService.replaceVinyl(id, newVinyl);
    }

    //I changed return type to ResponseEntity<String> to handle HTTP responses.
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVinyl(@PathVariable("id") int id) {
        boolean isDeleted = vinylService.deleteVinylById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Vinyl record with id " + id + " was successfully deleted.");
        } else {
            //I used ResponseEntity.status(HttpStatus.NOT_FOUND).body(...) to return a 404 status and a message if the vinyl record was not found.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vinyl record with id " + id + " was not found.");
        }
    }
}
