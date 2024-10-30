package s3.individual.vinylo.Models.services;

import java.util.List;

import s3.individual.vinylo.serviceIMPL.domain.Vinyl;

public interface VinylService {

    Vinyl createNewVinyl(Vinyl newvinyl);

    Vinyl getVinylById(int id);

    List<Vinyl> getVinyls();

    Vinyl replaceVinyl(int id, Vinyl newVinyl);

    boolean deleteVinylById(int id);

}