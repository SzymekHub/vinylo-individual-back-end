package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.persistence.entity.StateEnum;

public interface VinylRepo {

    List<Vinyl> getVinyls();

    Vinyl getVinylById(int id);

    Vinyl saveVinyl(Vinyl vinyl);

    Vinyl getByState(StateEnum state);

    boolean deleteVinylById(int id);
}
