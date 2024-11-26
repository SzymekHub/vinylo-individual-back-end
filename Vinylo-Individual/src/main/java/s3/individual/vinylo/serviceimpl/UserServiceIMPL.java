package s3.individual.vinylo.serviceimpl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.persistence.UserRepo;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User saveUser(Integer id, User newuser) {
        try {
            if (id != null) {
                // check if user exists already
                User existingUser = userRepo.getUserById(id);
                if (existingUser == null) {
                    throw new CustomNotFoundException(
                            "User with ID " + id + " was not found. A new user will be created");
                }
                // Update the existing user with the new details
                existingUser.setUsername(newuser.getUsername());
                existingUser.setEmail(newuser.getEmail());
                existingUser.setRole(newuser.getRole());

                // Save the updated user to the db
                return userRepo.saveUser(existingUser);
            } else {
                // save the user to the db
                // Encode the password before saving
                String encodedPassword = passwordEncoder.encode(newuser.getPassword());
                User newUser = User.builder()
                        .username(newuser.getUsername())
                        .email(newuser.getEmail())
                        .password(encodedPassword)
                        .role(newuser.getRole())
                        .build();

                return userRepo.saveUser(newUser);
            }
        } catch (Exception ex) {
            throw new CustomInternalServerErrorException("Failed to save the user " + ex.getMessage());
        }
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
