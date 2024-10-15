package s3.individual.vinylo.persistenceIMPL;

import org.springframework.stereotype.Repository;

import s3.individual.vinylo.Models.persistence.UserRepo;
import s3.individual.vinylo.services.domain.User;

import java.util.*;

//injection using spring. "Hey this is the repo"
@Repository
public class UserRepoImpl implements UserRepo {


    private final List<User> allUsers;

    public UserRepoImpl() {
        allUsers = CreateSomeUsers();
    }

    private List<User> CreateSomeUsers() {
        List<User> users = new ArrayList<>();        
        users.add(new User(0, "Premium User 1", "PremiumUser@gmail.com", "I am premium", true));
        users.add(new User(1, "Regular user", "RegularUser@gmail.com", "I am a regular", false));

        return users;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(allUsers);
    }

    @Override
    public User getUserById(int id) {
        for (User u : allUsers) {
            if (u.getId() == (id)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return allUsers.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst().orElse(null);
    }

    @Override
    public User createNewUser(User newUser) {
        allUsers.add(newUser);
        return newUser;
    }

    @Override
    public boolean deativateUserById(int id) {
        return  allUsers.removeIf(u -> u.id == (id));
    }

}