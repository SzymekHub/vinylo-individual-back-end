package s3.individual.vinylo.repositories;

import org.springframework.stereotype.Repository;
import s3.individual.vinylo.domain.Vinyl;

import java.util.ArrayList;

@Repository
public class VinylRepo {

    private final ArrayList<Vinyl> vinyls;

    public VinylRepo() {
        vinyls = new ArrayList<>();
        vinyls.add(new Vinyl("EP", "ALL RED", "I AM MUSIC", false ));
        vinyls.add(new Vinyl("LP", "Rubber Soul", "Rock&Roll", true ));
    }

    //The getVinyls() method is explicitly defined, replacing the need for the @Getter annotation from Lombok
    //But I could add the @Getter. By adding @Getter, Lombok generates this method for you,
    //allowing other classes to access the vinyls list without needing to explicitly define a getter method.
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
