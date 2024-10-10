package s3.individual.vinylo.Models.controllers;

import org.springframework.http.ResponseEntity;

import s3.individual.vinylo.Models.dtos.VinylDTO;
import s3.individual.vinylo.Models.dtos.VinylsDTO;

public interface VinylController {

    VinylsDTO getVinyls();

    ResponseEntity<?> getVinyl(int id);

    VinylDTO createNewVinyl(VinylDTO newVinyl);

    VinylDTO replaceVinyl(VinylDTO newVinyl, int id);

    ResponseEntity<String> deleteVinyl(int id);

}