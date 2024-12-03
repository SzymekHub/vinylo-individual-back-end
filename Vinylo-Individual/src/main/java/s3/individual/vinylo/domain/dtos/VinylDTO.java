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

    @NotBlank(message = "vinylType status is required")
    private String vinylType;

    @NotBlank(message = "speed status is required")
    private String speed;

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotBlank(message = "Description must not be blank")
    @Length(max = 150, message = "Description must not exceed 150 characters")
    private String description;

    @NotBlank(message = "state status is required")
    private String state;

    @NotBlank(message = "color status is required")
    private String color;

    @NotNull(message = "Release status is required")
    private Boolean isReleased;

    private int artist_id;

    private String spotifyAlbumId;

    public VinylDTO(int id, String title, String vinylType) {
        this.id = id;
        this.title = title;
        this.vinylType = vinylType;
    }
}
