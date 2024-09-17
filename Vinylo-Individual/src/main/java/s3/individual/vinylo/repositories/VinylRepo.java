package s3.individual.vinylo.repositories;

import org.springframework.stereotype.Repository;
import s3.individual.vinylo.domain.Vinyl;

import java.util.ArrayList;

@Repository
public class VinylRepo {

    private ArrayList<Vinyl> vinyls;

    public VinylRepo() {
        vinyls = new ArrayList<>();
        vinyls.add(new Vinyl("EP", "ALL RED", "I AM MUSIC", false ));
        vinyls.add(new Vinyl("LP", "Rubber Soul", "Rock&Roll", true ));
    }

    public ArrayList<Vinyl> getVinyls() {
        return vinyls;
    }

    public Vinyl getVinylById(String id) {
        for (Vinyl v : vinyls) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }
}
