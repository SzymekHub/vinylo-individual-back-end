package s3.individual.vinylo.domain.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO {
    private int id;
    private int auctionId;
    private int userId;
    private double bidAmount;
    private LocalDateTime bidTime;
}