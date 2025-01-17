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
import s3.individual.vinylo.exceptions.DuplicateItemException;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(User newuser) {
        try {
            // Check if the user already exists
            if (newuser.getId() != null) {
                int id = newuser.getId();

                User existingUser = userRepo.getUserById(id);
                if (existingUser != null) {
                    throw new DuplicateItemException(
                            "User with ID " + id + " already exists.");
                }
            }
            // if no id is provided, create a new user to the db
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

        } catch (DuplicateItemException e) {
            throw e;
        } catch (Exception ex) {
            throw new CustomInternalServerErrorException("Failed to save the user " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        try {
            // Check if the user exists
            User userToUpdate = userRepo.getUserById(user.getId());

            if (userToUpdate == null) {
                throw new CustomNotFoundException(
                        "User with ID " + user.getId() + " was not found.");
            }

            // set the id so that it won't think that it's a new record.
            userToUpdate.setId(userToUpdate.getId());

            // Update the existing user with the new details
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setRole(user.getRole());

            // Save the updated user to the db

            return userRepo.updateUser(userToUpdate);

        } catch (CustomNotFoundException e) {
            throw e; // Re-throw the expected exception
        } catch (Exception e) {
            throw new CustomInternalServerErrorException(
                    "Failed to update the user: " + e.getMessage());
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