package s3.individual.vinylo.services;

import java.util.List;

import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.domain.dtos.UserDTO;

public interface UserService {

    User createUser(User newuser);

    UserDTO updateUser(User user);

    User getUserById(int id);

    List<User> getUsers();

    User findByUsername(String username);

    boolean deativateUserById(int id);

}