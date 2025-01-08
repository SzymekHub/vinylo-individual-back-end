package s3.individual.vinylo.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileDTO {

    private int id;

    private int user_id;

    private String bio;

    private int balance;

}
