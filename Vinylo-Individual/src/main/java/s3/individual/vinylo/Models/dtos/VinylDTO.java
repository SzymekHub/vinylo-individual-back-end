package s3.individual.vinylo.Models.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VinylDTO {

    public int id;
    public String vinylType;
    public String title;
    public String description;
    public Boolean isReleased;
    public ArtistDTO artist;
}
