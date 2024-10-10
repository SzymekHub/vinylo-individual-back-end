package s3.individual.vinylo.persistenceIMPL;

import org.springframework.stereotype.Repository;

import s3.individual.vinylo.Models.persistence.VinylRepo;
import s3.individual.vinylo.services.domain.Vinyl;

import java.util.ArrayList;
import java.util.List;

//injection using spring. "Hey this is the repo"
@Repository
public class VinylRepoImpl implements VinylRepo {

    private final List<Vinyl> allVinyls;


    public VinylRepoImpl() {
        allVinyls = CreateSomeVinyls();
    }

    private List<Vinyl> CreateSomeVinyls() {
        List<Vinyl> vinyls = new ArrayList<>();
        vinyls.add(new Vinyl(1, "EP","ALL RED", "I AM MUSIC", true, "PLAYBOI CARTI"));
        vinyls.add(new Vinyl(2, "LP","Rubber Soul", "Rock&Roll", true, "The Beatles" ));

        return vinyls;
    }

    //The getVinyls() method is explicitly defined, replacing the need for the @Getter annotation from Lombok
    //But I could add the @Getter. By adding @Getter, Lombok generates this method for you,
    //allowing other classes to access the vinyls list without needing to explicitly define a getter method.
    @Override
    public List<Vinyl> getVinyls() {
        return new ArrayList<>(allVinyls);
    }

    @Override
    public Vinyl getVinylById(int id) {
        for (Vinyl v : allVinyls) {
            if (v.id == (id)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public Vinyl createNewVinyl(Vinyl newVinyl) {
        allVinyls.add(newVinyl);
        return newVinyl;
    }

    @Override
    public boolean deleteVinylById(int id) {
        return allVinyls.removeIf(v -> v.id == (id));
    }
}
