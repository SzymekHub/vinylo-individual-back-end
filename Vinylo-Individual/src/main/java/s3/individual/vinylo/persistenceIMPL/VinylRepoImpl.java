package s3.individual.vinylo.persistenceIMPL;

import org.springframework.stereotype.Repository;

import s3.individual.vinylo.persistence.ArtistRepo;
import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;

import java.util.ArrayList;
import java.util.List;

//injection using spring. "Hey this is the repo"
@Repository
public class VinylRepoImpl implements VinylRepo {

    private final ArtistRepo artistRepo;
    private final List<Vinyl> allVinyls;

    public VinylRepoImpl(ArtistRepo aRepo) {
        this.artistRepo = aRepo;
        allVinyls = CreateSomeVinyls();
    }

    private List<Vinyl> CreateSomeVinyls() {
        List<Vinyl> vinyls = new ArrayList<>();

        Artist arist1 = artistRepo.getArtistById(0);
        Artist arist2 = artistRepo.getArtistById(1);

        vinyls.add(new Vinyl(0, "EP", "ALL RED", "I AM MUSIC", true, arist1));
        vinyls.add(new Vinyl(1, "LP", "Rubber Soul", "Rock&Roll", true, arist2));

        return vinyls;
    }

    // The getVinyls() method is explicitly defined, replacing the need for the
    // @Getter annotation from Lombok
    // But I could add the @Getter. By adding @Getter, Lombok generates this method
    // for you,
    // allowing other classes to access the vinyls list without needing to
    // explicitly define a getter method.
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
