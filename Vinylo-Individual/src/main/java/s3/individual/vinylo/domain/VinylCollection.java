package s3.individual.vinylo.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class VinylCollection {

    @NotNull
    private Integer id;
    private User user;
    private Vinyl vinyl;

    public VinylCollection(Integer id, User user, Vinyl vinyl) {
        this.id = id;
        this.user = user;
        this.vinyl = vinyl;
    }

}
