package s3.individual.vinylo.services.domain;

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
    private int id;
    private String username;
    private String email;
    private String password;
    private Boolean isPremium;

    public User(int id, String username, String email, String password, Boolean isPremium) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isPremium = isPremium;
    }
}