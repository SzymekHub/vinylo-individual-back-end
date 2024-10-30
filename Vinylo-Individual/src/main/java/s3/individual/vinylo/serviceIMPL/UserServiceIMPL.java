package s3.individual.vinylo.serviceIMPL;

import java.util.*;

import org.springframework.stereotype.Service;

import s3.individual.vinylo.Models.persistence.UserRepo;
import s3.individual.vinylo.Models.services.UserService;
import s3.individual.vinylo.serviceIMPL.domain.User;

@Service
public class UserServiceIMPL implements UserService {
    
    private final UserRepo userRepo;
    
    public UserServiceIMPL(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User createNewUser(User user) {
        return userRepo.createNewUser(user);
    }

    @Override
    public User getUserById(int id) {
        return userRepo.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.getUsers();
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public boolean deativateUserById(int id) {  
        return userRepo.deativateUserById(id);  
    }
    
}
