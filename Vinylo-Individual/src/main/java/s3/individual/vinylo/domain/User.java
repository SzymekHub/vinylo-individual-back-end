package s3.individual.vinylo.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class User {

    @NotNull
    public int id;
    public String username;
    public String email;
    public String password;
    public Boolean isPremium;

    public User(int id, String username, String email, String password, Boolean isPremium) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isPremium = isPremium;
    }
}