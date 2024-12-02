package s3.individual.vinylo.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class Vinyl {

    @NotNull
    private Integer id;
    private VinylTypeEnum vinylType;
    private SpeedEnum speed;
    private String title;
    private String description;
    private StateEnum state;
    private VinylColorEnum color;
    private Boolean isReleased;
    private Artist artist;

    public Vinyl(Integer id, VinylTypeEnum vinylType, SpeedEnum speed, String title, String description,
            StateEnum state,
            VinylColorEnum color,
            Boolean isReleased,
            Artist artist) {
        this.id = id;
        this.vinylType = vinylType;
        this.speed = speed;
        this.title = title;
        this.description = description;
        this.state = state;
        this.color = color;
        this.isReleased = isReleased;
        this.artist = artist;
    }
}
