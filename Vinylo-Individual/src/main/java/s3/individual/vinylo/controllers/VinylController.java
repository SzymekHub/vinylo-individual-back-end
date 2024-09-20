package s3.individual.vinylo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import s3.individual.vinylo.controllers.dtos.VinylDTO;
import s3.individual.vinylo.controllers.dtos.VinylsDTO;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.repositories.VinylRepo;
import s3.individual.vinylo.services.VinylService;

import java.util.ArrayList;

@RestController
@RequestMapping("/vinyls")
public class VinylController {

    @Autowired
    private VinylService vinylService;

    @GetMapping()
    public VinylsDTO getVinyls(@RequestParam (required = false) Boolean hasArtist)
    {
        ArrayList<Vinyl> vinyls = vinylService.getVinyls(hasArtist);

        VinylsDTO result = new VinylsDTO();

        for (Vinyl v: vinyls)
        {
            VinylDTO vd = new VinylDTO();
            vd.aristName = v.getAristName();
            vd.isReleased = v.getisReleased();
            vd.description = v.getDescription();
            vd.name = v.getName();
            vd.id = v.getId();
            vd.albumCapacity = v.getAlbumCapacity();
            result.vinyls.add(vd);
        }

        return result;
    }

    @GetMapping("/{id}")
    public VinylDTO getVinyl(@PathVariable String id) {

        Vinyl v = vinylService.getVinylById(id);

        VinylDTO vd = new VinylDTO();
        vd.aristName = v.getAristName();
        vd.isReleased = v.getisReleased();
        vd.description = v.getDescription();
        vd.name = v.getName();
        vd.albumCapacity = v.getAlbumCapacity();
        vd.id = v.getId();

        return vd;
    }

    @PostMapping()
    public Vinyl createNewVinyl(@RequestBody Vinyl newVinyl) {
        return vinylService.createNewVinyl(newVinyl);
    }

    @PutMapping("/{id}")
    public Vinyl replaceVinyl(@RequestBody Vinyl newVinyl, @PathVariable String id) {

        return vinylService.replaceVinyl(id, newVinyl);
    }

    @DeleteMapping("/{id}")
    public String deleteVinyl(@PathVariable String id) {
        boolean isDeleted = vinylService.deleteVinylById(id);
        if (isDeleted) {
            return "Vinyl record with id " + id + " was successfully deleted.";
        } else {
            return "Vinyl record with id " + id + " was not found.";
        }
    }
}
