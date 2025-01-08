package s3.individual.vinylo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class Profile {

    private Integer id;
    private User user;
    private String bio;
    private int balance;

    public Profile(Integer id, User user, String bio, int balance) {
        this.id = id;
        this.user = user;
        this.bio = bio;
        this.balance = balance;
    }
}