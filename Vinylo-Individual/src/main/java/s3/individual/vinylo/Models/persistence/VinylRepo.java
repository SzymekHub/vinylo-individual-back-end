package s3.individual.vinylo.Models.persistence;


import java.util.List;

import s3.individual.vinylo.serviceIMPL.domain.Vinyl;

public interface VinylRepo {

    List<Vinyl> getVinyls();

    Vinyl getVinylById(int id);

    Vinyl createNewVinyl(Vinyl vinyl);

    boolean deleteVinylById(int id);
}
