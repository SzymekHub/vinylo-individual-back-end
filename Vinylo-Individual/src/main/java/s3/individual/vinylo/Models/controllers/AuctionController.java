package s3.individual.vinylo.Models.controllers;

import org.springframework.http.ResponseEntity;
import s3.individual.vinylo.Models.dtos.AuctionDTO;
import s3.individual.vinylo.Models.dtos.AuctionsDTO;

public interface AuctionController {

    AuctionsDTO geAuctions();

    ResponseEntity<?> getAuction(int id);

    AuctionDTO creAuction(AuctionDTO newAuction);

}