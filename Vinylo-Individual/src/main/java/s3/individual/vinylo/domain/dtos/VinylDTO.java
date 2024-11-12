package s3.individual.vinylo.domain.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private int id;

    @NotBlank(message = "vinylType must not be blank")
    private String vinylType;

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotBlank(message = "Description must not be blank")
    @Length(max = 150, message = "Description must not exceed 150 characters")
    private String description;

    @NotNull(message = "Release status is required")
    private Boolean isReleased;

    private int artist_id;
}
