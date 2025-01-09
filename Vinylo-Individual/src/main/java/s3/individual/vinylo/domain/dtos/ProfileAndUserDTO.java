package s3.individual.vinylo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileAndUserDTO {
    private ProfileDTO profile;
    private UserDTO user;
}
