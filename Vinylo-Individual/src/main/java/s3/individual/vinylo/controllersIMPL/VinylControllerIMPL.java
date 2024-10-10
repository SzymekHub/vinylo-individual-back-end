package s3.individual.vinylo.controllersIMPL;

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
import s3.individual.vinylo.Exceptions.CustomNotFoundException;
import s3.individual.vinylo.Models.Mappers.VinylMapper;
import s3.individual.vinylo.Models.controllers.VinylController;
import s3.individual.vinylo.Models.dtos.VinylDTO;
import s3.individual.vinylo.Models.dtos.VinylsDTO;
import s3.individual.vinylo.services.VinylService;

import java.util.List;

@RestController
@RequestMapping("/vinyls")

public class VinylControllerIMPL implements VinylController {

    @Autowired
    private VinylService vinylService;

    @Override
    @GetMapping()
    public VinylsDTO getVinyls()
    {
        List<Vinyl> vinyls = vinylService.getVinyls();

        VinylsDTO result = VinylMapper.toVinylsDTO(vinyls);
        return result;
    }

    //I changed return type to ResponseEntity<?> to handle HTTP responses.
    //This allows the method to return both a ResponseEntity<VinylDTO> when a valid vinyl record is found, 
    // and a ResponseEntity<String> when an error occurs (e.g., vinyl not found).
    @Override
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

    @Override
    @PostMapping()
    public VinylDTO createNewVinyl(@RequestBody @Valid VinylDTO newVinylDTO) {
        Vinyl newVinyl = VinylMapper.toVinyl(newVinylDTO);
        Vinyl cretedVinyl = vinylService.createNewVinyl(newVinyl);
        return VinylMapper.toVinylDTO(cretedVinyl);
    }

    @Override
    @PutMapping("{id}")
    public VinylDTO replaceVinyl(@RequestBody VinylDTO newVinylDTO, @PathVariable("id") int id) {
        Vinyl newVinyl = VinylMapper.toVinyl(newVinylDTO);
        Vinyl replacedVinyl = vinylService.replaceVinyl(id, newVinyl);
        return VinylMapper.toVinylDTO(replacedVinyl);
    }

    //I changed return type to ResponseEntity<String> to handle HTTP responses.
    @Override
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
