package s3.individual.vinylo.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private int id;

    @NotBlank(message = "username must not be blank")
    private String username;

    @NotBlank(message = "email must not be blank")
    private String email;

    @NotBlank(message = "password must not be blank")
    private String password;

    @NotNull(message = "Role status is required")
    private String role;

}
