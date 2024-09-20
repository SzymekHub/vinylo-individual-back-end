package s3.individual.vinylo.persistence;

import org.springframework.stereotype.Repository;
import s3.individual.vinylo.business.Vinyl;
import s3.individual.vinylo.persistence.interfaces.VinylRepo;

import java.util.ArrayList;

//injection using spring. "Hey this is the repo"
@Repository
public class VinylRepoImpl implements VinylRepo {

    private final ArrayList<Vinyl> vinyls;

    public VinylRepoImpl() {
        vinyls = new ArrayList<>();
        vinyls.add(new Vinyl(1, "EP","ALL RED", "I AM MUSIC", true, "PLAYBOI CARTI"));
        vinyls.add(new Vinyl(2, "LP","Rubber Soul", "Rock&Roll", true, "The Beatles" ));
    }

    //The getVinyls() method is explicitly defined, replacing the need for the @Getter annotation from Lombok
    //But I could add the @Getter. By adding @Getter, Lombok generates this method for you,
    //allowing other classes to access the vinyls list without needing to explicitly define a getter method.
    @Override
    public ArrayList<Vinyl> getVinyls() {
        return vinyls;
    }

    @Override
    public Vinyl getVinylById(int id) {
        for (Vinyl v : vinyls) {
            if (v.getId() == (id)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public Vinyl createNewVinyl(Vinyl newVinyl) {
        vinyls.add(newVinyl);
        return newVinyl;
    }

    @Override
    public boolean deleteVinylById(int id) {
        return vinyls.removeIf(v -> v.getId() == (id));
    }
}
