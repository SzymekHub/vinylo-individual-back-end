package s3.individual.vinylo.domain.dtos;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionsDTO {
    private List<AuctionDTO> auctions = new ArrayList<>();
}
