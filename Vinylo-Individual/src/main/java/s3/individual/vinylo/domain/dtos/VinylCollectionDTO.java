package s3.individual.vinylo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VinylCollectionDTO {

    private int id;

    private int user_id;

    private int vinyl_id;

}
