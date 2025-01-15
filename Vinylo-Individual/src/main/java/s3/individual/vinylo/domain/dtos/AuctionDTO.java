package s3.individual.vinylo.domain.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
// I needed and AllArgsConstructor for the tests\
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDTO {
    private int id;

    private String title;

    private int vinyl_id;

    private int seller_id;

    private String description;

    private double startingPrice;

    private double currentPrice;

    private LocalDate startTime;

    private LocalDate endTime;
}