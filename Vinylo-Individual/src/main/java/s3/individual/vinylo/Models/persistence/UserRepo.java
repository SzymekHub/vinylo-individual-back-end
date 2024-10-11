package s3.individual.vinylo.Models.persistence;

import s3.individual.vinylo.services.domain.User;

import java.util.*;

public interface UserRepo {

    List<User> getUsers();

    User getUserById(int id);

    User createNewUser(User user);

    User findByUsername(String username);

    boolean deativateUserById(int id);
}
