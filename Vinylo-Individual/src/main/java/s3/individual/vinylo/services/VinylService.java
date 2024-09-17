package s3.individual.vinylo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.repositories.VinylRepo;

import java.util.ArrayList;

@Service
public class VinylService {

    @Autowired
    private VinylRepo repo;

    public Vinyl getVinylById(String id) { return new Vinyl("LP", "ALL RED", "I AM MUSIC", true);}

    public ArrayList<Vinyl> getVinyls()
    {
        ArrayList<Vinyl> result = new ArrayList<Vinyl>();

        result.add(new Vinyl("EP", "ALL RED", "I AM MUSIC", false ));
        result.add(new Vinyl("LP", "Rubber Soul", "Rock&Roll", true ));

        return result;
    }

    public ArrayList<Vinyl> getVinyls(Boolean hasArtist)
    {
        ArrayList<Vinyl> vinyls = this.getVinyls();
        ArrayList<Vinyl> result = new ArrayList<>();

        if (hasArtist) {
            for (Vinyl v : vinyls) {
                if (v.getHasArtist() == hasArtist) {
                    result.add(v);
                }
            }
            return result;
        }
        return vinyls;
    }
}
