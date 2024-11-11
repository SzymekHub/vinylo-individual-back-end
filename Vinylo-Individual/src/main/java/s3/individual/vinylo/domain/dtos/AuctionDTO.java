package s3.individual.vinylo.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionDTO {
    private int id;
    private String title;
    private VinylDTO vinyl;
    private UserDTO seller;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private String startTime;
    private String endTime;

}
