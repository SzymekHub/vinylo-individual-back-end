package s3.individual.vinylo.domain;

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
    public int id;
    public String title;
    public Vinyl Vinyl;
    public User seller;
    public String description;
    public double startingPrice;
    public double currentPrice;
    public String startTime;
    public String endTime;

    public Auction(int id, String title, Vinyl Vinyl, User seller, String description, double startingPrice,
            double currentPrice, String startTime, String endTime) {
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
