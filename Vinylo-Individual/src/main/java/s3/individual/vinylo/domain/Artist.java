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
public class Artist {
    @NotNull
    private int id;
    private String name;
    private String bio;

    public Artist(int id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

}
