package s3.individual.vinylo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import s3.individual.vinylo.controllers.dtos.VinylDTO;
import s3.individual.vinylo.controllers.dtos.VinylsDTO;
import s3.individual.vinylo.domain.Vinyl;
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
        if (hasArtist == null)
        {
            hasArtist = false;
        }

        ArrayList<Vinyl> vinyls = vinylService.getVinyls(hasArtist);

        VinylsDTO result = new VinylsDTO();

        for (Vinyl v: vinyls)
        {
            VinylDTO vd = new VinylDTO();
            vd.hasArtist = v.getHasArtist();
            vd.description = v.getDescription();
            vd.name = v.getName();
            vd.id = v.getId();
            result.vinyls.add(vd);
        }

        return result;
    }

    @GetMapping("/{id}")
    public VinylDTO getVinyl(@PathVariable String id) {

        Vinyl v = vinylService.getVinylById(id);

        VinylDTO vd = new VinylDTO();
        vd.hasArtist = v.getHasArtist();
        vd.description = v.getDescription();
        vd.name = v.getName();
        vd.id = v.getId();

        return vd;
    }
}
