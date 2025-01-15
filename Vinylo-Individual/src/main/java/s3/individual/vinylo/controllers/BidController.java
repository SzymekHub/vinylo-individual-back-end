package s3.individual.vinylo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.domain.dtos.PlaceBidDTO;
import s3.individual.vinylo.services.BidService;

@RestController
@RequestMapping("/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping("/{auctionId}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<String> placeBid(@PathVariable("auctionId") int auctionId,
            @Valid @RequestBody PlaceBidDTO placeBidDTO) {
        boolean bidPlaced = bidService.placeBid(auctionId, placeBidDTO.getUserId(), placeBidDTO.getBidAmount());

        if (bidPlaced) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Bid placed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to place bid.");
        }
    }
}