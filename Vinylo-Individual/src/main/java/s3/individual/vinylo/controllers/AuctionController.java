package s3.individual.vinylo.controllers;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.domain.Mappers.AuctionMapper;
import s3.individual.vinylo.domain.dtos.AuctionDTO;
import s3.individual.vinylo.domain.dtos.AuctionsDTO;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.domain.Auction;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping()
    public AuctionsDTO geAuctions() {

        List<Auction> auctions = auctionService.getAuctions();

        return AuctionMapper.toAuctionsDTO(auctions);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAuction(@PathVariable("id") int id) {
        Auction a = auctionService.getAuctionsById(id);

        if (a == null) {
            throw new CustomNotFoundException("Auction record not found");
        }

        AuctionDTO ad = AuctionMapper.toAuctionDTO(a);

        return ResponseEntity.ok(ad);

    }

    @PostMapping()
    public AuctionDTO creAuction(@RequestBody @Valid AuctionDTO newAuction) {
        Auction newAuctions = AuctionMapper.toAuction(newAuction);
        Auction createdAuction = auctionService.createAuction(newAuctions);
        return AuctionMapper.toAuctionDTO(createdAuction);
    }
}
