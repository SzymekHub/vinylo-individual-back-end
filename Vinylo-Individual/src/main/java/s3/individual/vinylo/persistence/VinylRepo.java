package s3.individual.vinylo.persistence;

import java.util.List;

import s3.individual.vinylo.domain.Vinyl;

public interface VinylRepo {

    List<Vinyl> getVinyls(int offset, int limit);

    Vinyl getVinylById(int id);

    Vinyl saveVinyl(Vinyl vinyl);

    Vinyl findByArtistAndTitleAndState(int artistId, String title, String state);

    int getTotalVinylsCount();

    boolean deleteVinylById(int id);
}
