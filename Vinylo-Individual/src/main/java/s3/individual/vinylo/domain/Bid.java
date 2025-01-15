package s3.individual.vinylo.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Bid {

    @NotNull
    private Integer id;
    @NotNull
    private Integer auctionId;
    @NotNull
    private Integer userId;
    @NotNull
    private double bidAmount;
    private LocalDateTime bidTime;

    public Bid(Integer id, Integer auctionId, Integer userId, double bidAmount, LocalDateTime bidTime) {
        this.id = id;
        this.auctionId = auctionId;
        this.userId = userId;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
    }
}