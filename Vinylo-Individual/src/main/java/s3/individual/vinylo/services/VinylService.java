package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Vinyl;

public interface VinylService {

    Vinyl createNewVinyl(Vinyl newvinyl);

    Vinyl getVinylById(int id);

    List<Vinyl> getVinyls();

    Vinyl replaceVinyl(int id, Vinyl newVinyl);

    boolean deleteVinylById(int id);

}