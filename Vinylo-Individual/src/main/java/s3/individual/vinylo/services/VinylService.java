package s3.individual.vinylo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.repositories.VinylRepo;

import java.util.ArrayList;

@Service
public class VinylService {

    private final VinylRepo vinylRepo;

    public VinylService(VinylRepo vinylRepo) {
        this.vinylRepo = vinylRepo;
    }

    public Vinyl createNewVinyl(Vinyl vinyl) {
        return  vinylRepo.createNewVinyl(vinyl);
    }

    public Vinyl getVinylById(String id)
    {
        return vinylRepo.getVinylById(id);
    }

    public ArrayList<Vinyl> getVinyls(Boolean hasArtist)
    {
        ArrayList<Vinyl> allVinyls = vinylRepo.getVinyls();

        if (hasArtist == null || !hasArtist) {
            return allVinyls;
        }

        return allVinyls;
//        return filterVinylsByArtist(allVinyls);
    }

//    private ArrayList<Vinyl> filterVinylsByArtist(ArrayList<Vinyl> vinyls) {
//        ArrayList<Vinyl> result = new ArrayList<>();
//
//        for (Vinyl vinyl : vinyls) {
//            if (vinyl.getisReleased()) {
//                result.add(vinyl);
//            }
//        }
//
//        return result;
//    }

    public Vinyl replaceVinyl(String id, Vinyl newVinyl) {
        Vinyl existingVinyl = vinylRepo.getVinylById(id);

        if (existingVinyl != null) {
            existingVinyl.setName(newVinyl.getName());
            existingVinyl.setDescription(newVinyl.getDescription());
            existingVinyl.setIsReleased(newVinyl.getisReleased());
            existingVinyl.setAristName(newVinyl.getAristName());
            existingVinyl.setAlbumCapacity(newVinyl.getAlbumCapacity());
            return existingVinyl;
        } else {
            return vinylRepo.createNewVinyl(newVinyl);
        }
    }


    public boolean deleteVinylById(String id) {
        return vinylRepo.deleteVinylById(id);
    }
}
