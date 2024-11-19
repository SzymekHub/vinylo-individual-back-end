package s3.individual.vinylo.domain.dtos;

import jakarta.validation.constraints.NotBlank;
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

    private int id;
    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "bio must not be blank")
    private String bio;

}
