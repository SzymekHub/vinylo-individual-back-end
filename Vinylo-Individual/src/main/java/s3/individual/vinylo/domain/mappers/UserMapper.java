package s3.individual.vinylo.domain.mappers;

import java.util.*;
import s3.individual.vinylo.domain.dtos.UserDTO;
import s3.individual.vinylo.domain.dtos.UsersDTO;
import s3.individual.vinylo.domain.User;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getIsPremium());
    }

    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setIsPremium(user.getIsPremium());
        return userDTO;
    }

    public static UsersDTO toUsersDTO(List<User> users) {
        UsersDTO usersDTO = new UsersDTO();
        for (User user : users) {
            usersDTO.getUsers().add(toUserDTO(user));
        }
        return usersDTO;
    }

    public static List<User> toUsers(UsersDTO usersDTO) {
        List<User> users = new ArrayList<>();
        for (UserDTO userDTO : usersDTO.getUsers()) {
            users.add(toUser(userDTO));
        }
        return users;
    }

}
