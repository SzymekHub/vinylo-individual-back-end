package s3.individual.vinylo.services.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Auction {

    @NotNull
    private int id;
    private String title;
    private Vinyl Vinyl;
    private User seller;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private String startTime;
    private String endTime;

    public Auction(int id, String title, Vinyl Vinyl, User seller, String description, double startingPrice, double currentPrice, String startTime, String endTime) {
        this.id = id;
        this.title = title;
        this.Vinyl = Vinyl;
        this.seller = seller;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
}
