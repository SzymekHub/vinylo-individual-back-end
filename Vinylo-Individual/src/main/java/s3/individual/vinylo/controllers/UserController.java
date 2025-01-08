package s3.individual.vinylo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.domain.mappers.UserMapper;
import s3.individual.vinylo.domain.dtos.UserDTO;
import s3.individual.vinylo.domain.dtos.UsersDTO;
import s3.individual.vinylo.services.ProfileService;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.User;

@RestController
@RequestMapping("/users")

public class UserController {

    private final UserService userService;
    private final ProfileService profileService;

    public UserController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @GetMapping()
    public UsersDTO getUsers() {
        List<User> users = userService.getUsers();
        return UserMapper.toUsersDTO(users);

    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") int id) {

        User u = userService.getUserById(id);

        if (u == null) {
            // If the user is not found, throw this specific exception
            throw new CustomNotFoundException("User record not found");
        }

        UserDTO ud = UserMapper.toUserDTO(u);

        return ResponseEntity.ok(ud);
    }

    @PostMapping()
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO newUserDTO) {

        User newuser = UserMapper.toUser(newUserDTO);
        // Save the user object
        User createdUser = userService.saveUser(null, newuser);
        profileService.saveProfile(null, new Profile(0, createdUser, "", 0));

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @DeleteMapping("{id}")
    @RolesAllowed({ "ADMIN" })
    public ResponseEntity<String> deativateUserById(@PathVariable("id") int id) {
        boolean isDeleted = userService.deativateUserById(id);
        if (isDeleted) {
            return ResponseEntity.ok("User with id " + id + " was successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " was not found.");
        }
    }
}