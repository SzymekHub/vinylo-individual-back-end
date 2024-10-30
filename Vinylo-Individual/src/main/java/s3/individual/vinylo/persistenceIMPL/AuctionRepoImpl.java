package s3.individual.vinylo.persistenceIMPL;

import java.util.*;

import org.springframework.stereotype.Repository;
import lombok.Getter;
import s3.individual.vinylo.Models.persistence.AuctionRepo;
import s3.individual.vinylo.Models.persistence.UserRepo;
import s3.individual.vinylo.Models.persistence.VinylRepo;
import s3.individual.vinylo.serviceIMPL.domain.Auction;
import s3.individual.vinylo.serviceIMPL.domain.User;
import s3.individual.vinylo.serviceIMPL.domain.Vinyl;

@Repository
@Getter
public class AuctionRepoImpl implements AuctionRepo {
    
    private final VinylRepo vinylRepo;
    private final UserRepo userRepo;
    private final List<Auction> allAuctions;


    public AuctionRepoImpl(VinylRepo vRepo, UserRepo uRepo) {
        this.vinylRepo = vRepo;
        this.userRepo = uRepo;
        allAuctions = CreateSomeAuctions();
    }

    private List<Auction> CreateSomeAuctions() {
     // Retrieve Vinyl and User by ID

     List<Auction> auctions = new ArrayList<>();
     Vinyl vinyl = vinylRepo.getVinylById(1);
     User user  = userRepo.getUserById(1);

     Vinyl vinyl2 = vinylRepo.getVinylById(2);
     User user2  = userRepo.getUserById(2);

     auctions.add(new Auction(0, "All new EP", vinyl, user, "PLAYBOI CARTI'S NEW EP", 666.00, 999.00, "25/09/2024", "01/10/2024"));
     auctions.add(new Auction(1, "All new LP", vinyl2, user2, "The Beatles greatest LP", 250.00, 360.00, "27/09/2024", "31/10/2024"));

     return auctions;
    }


    @Override
    public Auction getAuctionById(int id) {
        return allAuctions.stream()
        .filter(a -> a.getId() == id)
        .findFirst().orElse(null);
    }

    @Override
    public List<Auction> getAuctions() {
        return new ArrayList<>(allAuctions);
    }

    @Override
    public Auction createNewAuction(Auction auction) {
        // Use the repositories to fetch Vinyl and User objects dynamically
        Vinyl vinyl = vinylRepo.getVinylById(auction.getVinyl().getId());
        User seller = userRepo.getUserById(auction.getSeller().getId());

        if (vinyl == null || seller == null) {
            throw new IllegalArgumentException("Invalid Vinyl or User ID");
        }

        auction.setVinyl(vinyl);
        auction.setSeller(seller);
        allAuctions.add(auction);
        return auction;
    }

}
