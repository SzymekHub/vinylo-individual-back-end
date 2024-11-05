package s3.individual.vinylo.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    public int id;
    public String username;
    public String email;
    public String password;
    public Boolean isPremium;

}
