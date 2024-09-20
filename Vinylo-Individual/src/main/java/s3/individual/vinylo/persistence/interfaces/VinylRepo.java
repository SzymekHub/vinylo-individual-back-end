package s3.individual.vinylo.persistence.interfaces;

import s3.individual.vinylo.business.Vinyl;

import java.util.ArrayList;

public interface VinylRepo {

    ArrayList<Vinyl> getVinyls();

    Vinyl getVinylById(int id);

    Vinyl createNewVinyl(Vinyl vinyl);

    boolean deleteVinylById(int id);
}
