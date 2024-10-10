package s3.individual.vinylo.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import s3.individual.vinylo.persistence.dtos.AuctionDTO;
import s3.individual.vinylo.persistence.dtos.AuctionsDTO;
import s3.individual.vinylo.persistence.dtos.UserDTO;
import s3.individual.vinylo.persistence.dtos.VinylDTO;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.services.domain.Auction;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;
    
    @GetMapping()
    public AuctionsDTO geAuctionsDTO() {
        
        ArrayList<Auction> auctions = auctionService.getAuctions();

        AuctionsDTO result = new AuctionsDTO();

        for (Auction a: auctions) 
        {
            AuctionDTO ad = new AuctionDTO();
            ad.id = a.getId();
            ad.title = a.getTitle();

            // Initialize VinylDTO and UserDTO inside AuctionDTO
            ad.vinyl = new VinylDTO();  
            ad.seller = new UserDTO();  

            ad.vinyl.id = a.getVinyl().getId();
            ad.vinyl.vinylType = a.getVinyl().getvinylType();
            ad.vinyl.title = a.getVinyl().getTitle();
            ad.vinyl.description = a.getVinyl().getDescription();
            ad.vinyl.isReleased = a.getVinyl().getisReleased();
            ad.vinyl.artistName = a.getVinyl().getArtistName();

            ad.seller.id = a.getSeller().getId();
            ad.seller.username = a.getSeller().getUsername();
            ad.seller.isPremium = a.getSeller().getIsPremium();

            ad.description = a.getDescription();
            ad.startingPrice = a.getStartingPrice();
            ad.currentPrice = a.getCurrentPrice();
            ad.startTime = a.getStartTime();
            ad.endTime = a.getEndTime();

            result.auctions.add(ad);
    }
    
    return result;
    }

    @GetMapping("{id}")
    public AuctionDTO getAuction(@PathVariable("id") int id) 
    {
    
        Auction a = auctionService.getAuctionsById(id);

        AuctionDTO ad = new AuctionDTO();
        ad.id = a.getId();
        ad.title = a.getTitle();
        
        // Initialize VinylDTO and UserDTO inside AuctionDTO
        ad.vinyl = new VinylDTO();  
        ad.seller = new UserDTO();  

        ad.vinyl.id = a.getVinyl().getId();
        ad.vinyl.vinylType = a.getVinyl().getvinylType();
        ad.vinyl.title = a.getVinyl().getTitle();
        ad.vinyl.description = a.getVinyl().getDescription();
        ad.vinyl.isReleased = a.getVinyl().getisReleased();
        ad.vinyl.artistName = a.getVinyl().getArtistName();

        ad.seller.id = a.getSeller().getId();
        ad.seller.username = a.getSeller().getUsername();
        ad.seller.isPremium = a.getSeller().getIsPremium();

        ad.description = a.getDescription();
        ad.startingPrice = a.getStartingPrice();
        ad.currentPrice = a.getCurrentPrice();
        ad.startTime = a.getStartTime();
        ad.endTime = a.getEndTime();

        return ad;
        } 

    @PostMapping()
    public Auction creAuction(@RequestBody @Valid Auction newAuction) 
    {
        return auctionService.createAuction(newAuction);
    }
}
