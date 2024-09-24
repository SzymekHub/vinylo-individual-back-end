package s3.individual.vinylo.services.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
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