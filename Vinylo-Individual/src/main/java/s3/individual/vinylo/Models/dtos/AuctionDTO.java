package s3.individual.vinylo.Models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionDTO {
    public int id;
    public String title;
    public VinylDTO vinyl;
    public UserDTO seller;
    public String description;
    public double startingPrice;
    public double currentPrice;
    public String startTime;
    public String endTime;

}
