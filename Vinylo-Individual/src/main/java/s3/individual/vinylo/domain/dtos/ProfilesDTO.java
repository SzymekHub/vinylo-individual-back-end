package s3.individual.vinylo.domain.dtos;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilesDTO {

    private List<ProfileDTO> profiles = new ArrayList<>();

}
