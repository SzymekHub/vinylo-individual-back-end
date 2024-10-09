package s3.individual.vinylo.services;

import org.springframework.stereotype.Service;
import s3.individual.vinylo.services.domain.Vinyl;
import s3.individual.vinylo.services.interfaces.VinylRepo;

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

    public Vinyl getVinylById(int id)
    {
        return vinylRepo.getVinylById(id);
    }

    public ArrayList<Vinyl> getVinyls()
    {
        return vinylRepo.getVinyls();
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

    public Vinyl replaceVinyl(int id, Vinyl newVinyl) {
        Vinyl existingVinyl = vinylRepo.getVinylById(id);

        if (existingVinyl != null) {
            existingVinyl.setTitle(newVinyl.getTitle());
            existingVinyl.setDescription(newVinyl.getDescription());
            existingVinyl.setIsReleased(newVinyl.getisReleased());
            existingVinyl.setAristName(newVinyl.getAristName());
            existingVinyl.setVinylType(newVinyl.getvinylType());
            return existingVinyl;
        } else {
            return vinylRepo.createNewVinyl(newVinyl);
        }
    }


    public boolean deleteVinylById(int id) {
        return vinylRepo.deleteVinylById(id);
    }
}
