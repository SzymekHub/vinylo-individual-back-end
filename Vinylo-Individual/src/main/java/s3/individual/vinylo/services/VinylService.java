package s3.individual.vinylo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.repositories.VinylRepo;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class VinylService {

    @Autowired
    private VinylRepo repo;
    @Autowired
    private VinylRepo vinylRepo;

    //this is for when i wawnna fetch vinyl records by the id
    public Vinyl getVinylById(String id)
    {
        return repo.getVinylById(id);
    }

    public ArrayList<Vinyl> getVinyls(Boolean hasArtist)
    {
        ArrayList<Vinyl> allVinyls = vinylRepo.getVinyls();

        if (hasArtist == null || !hasArtist) {
            return allVinyls;
        }

        return filterVinylsByArtist(allVinyls);
    }

    private ArrayList<Vinyl> filterVinylsByArtist(ArrayList<Vinyl> vinyls) {
        ArrayList<Vinyl> result = new ArrayList<>();

        for (Vinyl vinyl : vinyls) {
            if (vinyl.getHasArtist()) {
                result.add(vinyl);
            }
        }

        return result;
    }
}
