package s3.individual.vinylo.services.interfaces;

import s3.individual.vinylo.services.domain.User;

import java.util.ArrayList;

public interface UserRepo {

    ArrayList<User> getUsers();

    User getUserById(int id);

    User createNewUser(User user);

    User findByUsername(String username);

    boolean deativateUserById(int id);
}
