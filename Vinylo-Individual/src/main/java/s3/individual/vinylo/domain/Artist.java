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
    private Integer id;
    private String name;
    private String bio;

    public Artist(Integer id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

    public String toString() {
        return "Id: " + id + " name: " + name + " bio: " + bio;
    }

}
