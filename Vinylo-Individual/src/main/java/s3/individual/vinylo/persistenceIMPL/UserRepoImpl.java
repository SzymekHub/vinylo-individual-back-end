package s3.individual.vinylo.persistenceIMPL;

import org.springframework.stereotype.Repository;

import lombok.Getter;
import s3.individual.vinylo.Models.persistence.UserRepo;
import s3.individual.vinylo.services.domain.User;

import java.util.ArrayList;

//injection using spring. "Hey this is the repo"
@Repository
@Getter
public class UserRepoImpl implements UserRepo {


    private final ArrayList<User> users;

    public UserRepoImpl() {
        users = new ArrayList<>();
        users.add(new User(1, "Premium User 1", "PremiumUser@gmail.com", "I am premium", true));
        users.add(new User(2, "Regular user", "RegularUser@gmail.com", "I am a regular", false));

    }

    // @Override
    // public ArrayList<User> getUsers() {
    //     return users;
    // }

    @Override
    public User getUserById(int id) {
        for (User u : users) {
            if (u.getId() == (id)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst().orElse(null);
    }

    @Override
    public User createNewUser(User newUser) {
        users.add(newUser);
        return newUser;
    }

    @Override
    public boolean deativateUserById(int id) {
        return users.removeIf(u -> u.getId() == (id));
    }

}