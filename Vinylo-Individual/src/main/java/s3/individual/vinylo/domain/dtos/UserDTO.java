package s3.individual.vinylo.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private int id;
    private String username;
    private String email;
    private String password;
    private Boolean isPremium;

}
