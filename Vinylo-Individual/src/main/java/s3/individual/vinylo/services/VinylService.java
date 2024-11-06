package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Vinyl;

public interface VinylService {
    Vinyl saveVinyl(int id, Vinyl newVinyl);

    Vinyl saveVinyl(Vinyl newVinyl); // Overloaded method for creating new vinyl

    Vinyl getVinylById(int id);

    List<Vinyl> getVinyls();

    boolean deleteVinylById(int id);

}