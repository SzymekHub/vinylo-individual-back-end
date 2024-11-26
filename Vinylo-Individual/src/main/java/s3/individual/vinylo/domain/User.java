package s3.individual.vinylo.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import s3.individual.vinylo.persistence.entity.RoleEnum;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class User {

    @NotNull
    private Integer id;
    private String username;
    private String email;
    private String password;
    private RoleEnum role;

    public User(Integer id, String username, String email, String password, RoleEnum role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}