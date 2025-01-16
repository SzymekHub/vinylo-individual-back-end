package s3.individual.vinylo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.domain.Bid;
import s3.individual.vinylo.domain.dtos.BidDTO;
import s3.individual.vinylo.domain.dtos.PlaceBidDTO;
import s3.individual.vinylo.domain.mappers.BidMapper;
import s3.individual.vinylo.services.BidService;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.services.AuctionService;

@RestController
@RequestMapping("/bids")
public class BidController {
    private final BidService bidService;
    private final AuctionService auctionService;
    private final SimpMessagingTemplate messagingTemplate;

    public BidController(BidService bidService, SimpMessagingTemplate messagingTemplate,
            AuctionService auctionService) {
        this.bidService = bidService;
        this.messagingTemplate = messagingTemplate;
        this.auctionService = auctionService;
    }

    @PostMapping("/{auctionId}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<BidDTO> placeBid(@Valid @RequestBody PlaceBidDTO placeBidDTO,
            @PathVariable("auctionId") int auctionId) {

        Bid bidPlaced = bidService.placeBid(auctionId, placeBidDTO.getUserId(), placeBidDTO.getBidAmount());
        BidDTO bidPlacedDTO = BidMapper.toBidDTO(bidPlaced);
        Auction auction = auctionService.getAuctionsById(auctionId);
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId, auction);
        return ResponseEntity.ok(bidPlacedDTO);
    }
}