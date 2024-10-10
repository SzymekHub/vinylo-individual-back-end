package s3.individual.vinylo.persistenceIMPL;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;
import lombok.Getter;
import s3.individual.vinylo.Models.persistence.AuctionRepo;
import s3.individual.vinylo.Models.persistence.UserRepo;
import s3.individual.vinylo.Models.persistence.VinylRepo;
import s3.individual.vinylo.services.domain.Auction;
import s3.individual.vinylo.services.domain.User;
import s3.individual.vinylo.services.domain.Vinyl;

@Repository
@Getter
public class AuctionRepoImpl implements AuctionRepo {
    
    private final VinylRepo vinylRepo;
    private final UserRepo userRepo;
    private final ArrayList<Auction> auctions;


    public AuctionRepoImpl(VinylRepo vRepo, UserRepo uRepo) {
        vinylRepo = vRepo;
        userRepo = uRepo;
        auctions = new ArrayList<>();

        // Retrieve Vinyl and User by ID
        Vinyl vinyl = vinylRepo.getVinylById(1);
        User user  = userRepo.getUserById(1);

        Vinyl vinyl2 = vinylRepo.getVinylById(2);
        User user2  = userRepo.getUserById(2);

        auctions.add(new Auction(1, "All new EP", vinyl, user, "PLAYBOI CARTI'S NEW EP", 666.00, 999.00, "25/09/2024", "01/10/2024"));
        auctions.add(new Auction(2, "All new LP", vinyl2, user2, "The Beatles greatest LP", 250.00, 360.00, "28/09/2024", "31/10/2024"));
    }


    @Override
    public Auction getAuctionById(int id) {
        return auctions.stream()
        .filter(a -> a.getId() == id)
        .findFirst().orElse(null);
    }

    @Override
    public ArrayList<Auction> getAuctions() {
        return auctions;
    }

    @Override
    public Auction save(Auction auction) {
        auctions.add(auction);
        return auction;
    }

}
