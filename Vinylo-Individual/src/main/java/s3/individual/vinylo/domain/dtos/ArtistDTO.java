package s3.individual.vinylo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
// I needed and AllArgsConstructor for the tests
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {

    public int id;
    public String name;
    public String bio;

}
