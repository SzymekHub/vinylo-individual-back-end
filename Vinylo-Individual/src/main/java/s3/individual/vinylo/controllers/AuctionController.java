package s3.individual.vinylo.controllers;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.domain.mappers.AuctionMapper;
import s3.individual.vinylo.domain.dtos.AuctionDTO;
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
    private final SimpMessagingTemplate messagingTemplate;

    public AuctionController(AuctionService auctionService, VinylService vinylService, UserService userService,
            SimpMessagingTemplate messagingTemplate) {
        this.auctionService = auctionService;
        this.vinylService = vinylService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;

    }

    @GetMapping()
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public Map<String, Object> getAuctions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        List<Auction> auctions = auctionService.getAuctions(page, size);

        int totalCount = auctionService.getTotalAuctionsCount();

        // Prepare the response with paginated data and metadata
        Map<String, Object> response = new HashMap<>();
        // Accesses the list within VinylsDTO
        response.put("auctions", AuctionMapper.toAuctionsSummaryDTO(auctions).getAuctions());
        // Adds totalCount of vinyls to the response
        response.put("totalCount", totalCount);
        // Adds the current page number to response.
        response.put("currentPage", page);
        // Calculates and add total pages.
        response.put("totalPages", (int) Math.ceil((double) totalCount / size));

        return response;
    }

    @GetMapping("{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<AuctionDTO> getAuction(@PathVariable("id") int id) {

        Auction auction = auctionService.getAuctionsById(id);

        if (auction == null) {
            throw new CustomNotFoundException("Auction record not found");
        }

        AuctionDTO ad = AuctionMapper.toAuctionDTO(auction);

        return ResponseEntity.ok(ad);

    }

    @PostMapping()
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
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

        Auction auction = AuctionMapper.toAuction(newAuctionDTO);

        auction.setVinyl(vinyl);
        auction.setSeller(seller);

        auctionService.createAuction(auction);

        return ResponseEntity.status(HttpStatus.CREATED).body("Auction was created successfully");
    }

    @PutMapping("{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<AuctionDTO> replaceAuctionDescription(@RequestBody AuctionDTO auctionDTO,
            @PathVariable("id") int id) {

        Auction updatedAuction = auctionService.updateAuction(id, auctionDTO);
        AuctionDTO updatedAuctionDTO = AuctionMapper.toAuctionDTO(updatedAuction);
        messagingTemplate.convertAndSend("/topic/auction/" + id, updatedAuction);
        return ResponseEntity.ok(updatedAuctionDTO);
    }

    @DeleteMapping("{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<String> deativateAuctionById(@PathVariable("id") int id) {
        boolean isDeleted = auctionService.deativateAuctionById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Auction with id " + id + " was successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auction with id " + id + " was not found.");
        }
    }
}