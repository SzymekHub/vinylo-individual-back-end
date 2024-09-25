package s3.individual.vinylo.persistence.dtos;


public class AuctionDTO {
    public int id;
    public String title;
    public VinylDTO Vinyl;
    public UserDTO seller;
    public String description;
    public double startingPrice;
    public double currentPrice;
    public String startTime;
    public String endTime;

}
