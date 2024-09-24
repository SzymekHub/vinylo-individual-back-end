package s3.individual.vinylo.services.interfaces;

import s3.individual.vinylo.services.domain.Vinyl;

import java.util.ArrayList;

public interface VinylRepo {

    ArrayList<Vinyl> getVinyls();

    Vinyl getVinylById(int id);

    Vinyl createNewVinyl(Vinyl vinyl);

    boolean deleteVinylById(int id);
}
