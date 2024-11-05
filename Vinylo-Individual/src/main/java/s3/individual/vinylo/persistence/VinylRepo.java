package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.Vinyl;

public interface VinylRepo {

    List<Vinyl> getVinyls();

    Vinyl getVinylById(int id);

    Vinyl createNewVinyl(Vinyl vinyl);

    boolean deleteVinylById(int id);
}
