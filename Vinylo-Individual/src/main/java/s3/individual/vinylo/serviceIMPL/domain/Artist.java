package s3.individual.vinylo.serviceIMPL.domain;

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
    public int id;
    public String name;
    public String bio;

    public Artist(int id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

}
