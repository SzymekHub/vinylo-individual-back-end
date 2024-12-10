package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.Vinyl;

public interface VinylService {
    // I use Integer here so that it can allow nulls, so i can make new vinyls
    // Integer is a wrapper class for int, which means it can hold an integer value
    // but also allows for null.
    Vinyl saveVinyl(Integer id, Vinyl newVinyl);

    Vinyl getVinylById(int id);

    List<Vinyl> getVinyls(int page, int size);

    int getTotalVinylsCount();

    boolean deleteVinylById(int id);

}