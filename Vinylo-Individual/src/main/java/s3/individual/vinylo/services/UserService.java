package s3.individual.vinylo.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.services.domain.User;
import s3.individual.vinylo.services.interfaces.UserRepo;

@Service
public class UserService {
    
    private final UserRepo userRepo;
    
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserById(int id) {
        return userRepo.getUserById(id);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User createNewUser(User user) {
        return userRepo.createNewUser(user);
    }

    public boolean deativateUserById(int id) {  
        return userRepo.deativateUserById(id);  
    }

    public ArrayList<User> getUsers() {
        return userRepo.getUsers();
    }
    
}
