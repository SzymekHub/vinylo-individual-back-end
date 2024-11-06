package s3.individual.vinylo.services.serviceMocks;

import java.util.ArrayList;

import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.domain.Vinyl;

public class MockVinylRepo implements VinylRepo {

    ArrayList<Vinyl> vinyls = new ArrayList<>();

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
    public Vinyl saveVinyl(Vinyl newVinyl) {
        vinyls.add(newVinyl);
        return newVinyl;
    }

    @Override
    public boolean deleteVinylById(int id) {

        return vinyls.removeIf(v -> v.getId() == (id));
    }

}
