package s3.individual.vinylo.Models.persistence;

import java.util.*;

import s3.individual.vinylo.serviceIMPL.domain.User;

public interface UserRepo {

    List<User> getUsers();

    User getUserById(int id);

    User createNewUser(User user);

    User findByUsername(String username);

    boolean deativateUserById(int id);
}
