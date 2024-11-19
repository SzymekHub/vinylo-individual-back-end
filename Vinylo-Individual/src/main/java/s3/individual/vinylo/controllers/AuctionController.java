package s3.individual.vinylo.controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.domain.mappers.AuctionMapper;
import s3.individual.vinylo.domain.dtos.AuctionDTO;
import s3.individual.vinylo.domain.dtos.AuctionsDTO;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.User;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final VinylService vinylService;
    private final UserService userService;

    public AuctionController(AuctionService auctionService, VinylService vinylService, UserService userService) {
        this.auctionService = auctionService;
        this.vinylService = vinylService;
        this.userService = userService;
    }

    @GetMapping()
    public AuctionsDTO geAuctions() {
        List<Auction> auctions = auctionService.getAuctions();

        return AuctionMapper.toAuctionsDTO(auctions);
    }

    @GetMapping("{id}")
    public ResponseEntity<AuctionDTO> getAuction(@PathVariable("id") int id) {

        Auction auction = auctionService.getAuctionsById(id);

        if (auction == null) {
            throw new CustomNotFoundException("Auction record not found");
        }

        AuctionDTO ad = AuctionMapper.toAuctionDTO(auction);

        return ResponseEntity.ok(ad);

    }

    @PostMapping()
    public ResponseEntity<?> addAuction(@Valid @RequestBody AuctionDTO newAuctionDTO) {
        Vinyl vinyl = vinylService.getVinylById(newAuctionDTO.getVinyl_id());
        User seller = userService.getUserById(newAuctionDTO.getSeller_id());

        if (vinyl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vinyl with ID " + newAuctionDTO.getVinyl_id() + " not found.");
        }

        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Seller with ID " + newAuctionDTO.getSeller_id() + " not found.");
        }

        System.out.println("Received auction: " + newAuctionDTO);
        Auction auction = AuctionMapper.toAuction(newAuctionDTO);

        auction.setVinyl(vinyl);
        auction.setSeller(seller);

        auctionService.saveAuction(null, auction);

        return ResponseEntity.status(HttpStatus.CREATED).body("Auction was created successfully");
    }

    @PutMapping("{id}")
    public AuctionDTO replaceAuctionDescription(@RequestBody AuctionDTO newAuction, @PathVariable("id") int id) {
        Auction newAuctions = AuctionMapper.toAuction(newAuction);
        Auction createdAuction = auctionService.saveAuction(id, newAuctions);
        return AuctionMapper.toAuctionDTO(createdAuction);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deativateAuctionById(@PathVariable("id") int id) {
        boolean isDeleted = auctionService.deativateAuctionById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Auction with id " + id + " was successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auction with id " + id + " was not found.");
        }
    }
}
