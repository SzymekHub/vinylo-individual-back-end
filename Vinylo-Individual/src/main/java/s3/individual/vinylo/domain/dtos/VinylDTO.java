package s3.individual.vinylo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
// I Use @Builder for VinylDTO because it gives me more flexibility and is
// better
// suited for complex or mutable objects that might evolve over time.
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VinylDTO {

    public int id;
    public String vinylType;
    public String title;
    public String description;
    public Boolean isReleased;
    public ArtistDTO artist;
}
