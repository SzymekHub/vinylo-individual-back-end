package s3.individual.vinylo.persistence.entity;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auction")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vinyl_id")
    private VinylEntity vinyl;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity seller;

    @Column(name = "description")
    @Length(max = 150)
    private String description;

    @NotNull
    @Column(name = "startingPrice")
    private double startingPrice;

    @NotNull
    @Column(name = "currentPrice")
    private double currentPrice;

    @NotNull
    @Column(name = "startTime")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate startTime;

    @NotNull
    @Column(name = "endTime")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate endTime;

    @Override
    public String toString() {
        return "AuctionEntity{id=" + id + ", title='" + title + "', description='" + description + "', startingPrice="
                + startingPrice + ", currentPrice=" + currentPrice + ", startTime='" + startTime + "', endTime='"
                + endTime + "'}";
    }
}
