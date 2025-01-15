package s3.individual.vinylo.domain;

import java.time.LocalDate;

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
    private Integer id;
    private String title;
    private Vinyl vinyl;
    private User seller;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private LocalDate startTime;
    private LocalDate endTime;
    private String auctionStatus;

    public Auction(Integer id, String title, Vinyl vinyl, User seller, String description, double startingPrice,
            double currentPrice, LocalDate startTime, LocalDate endTime, String auctionStatus) {
        this.id = id;
        this.title = title;
        this.vinyl = vinyl;
        this.seller = seller;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.auctionStatus = auctionStatus;
    }

}