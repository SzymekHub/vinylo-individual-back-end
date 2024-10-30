package s3.individual.vinylo.Models.services;

import java.util.List;

import s3.individual.vinylo.serviceIMPL.domain.User;

public interface UserService {

    User createNewUser(User user);

    User getUserById(int id);

    List<User> getUsers();

    User findByUsername(String username);

    boolean deativateUserById(int id);

}