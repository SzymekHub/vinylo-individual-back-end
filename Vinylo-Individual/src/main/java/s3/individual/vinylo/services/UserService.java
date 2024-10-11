package s3.individual.vinylo.services;

import java.util.*;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.UserRepo;
import s3.individual.vinylo.services.domain.User;

@Service
public class UserService {
    
    private final UserRepo userRepo;
    
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User createNewUser(User user) {
        return userRepo.createNewUser(user);
    }

    public User getUserById(int id) {
        return userRepo.getUserById(id);
    }

    public List<User> getUsers() {
        return userRepo.getUsers();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public boolean deativateUserById(int id) {  
        return userRepo.deativateUserById(id);  
    }
    
}
