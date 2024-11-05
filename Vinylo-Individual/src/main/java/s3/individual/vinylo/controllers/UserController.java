package s3.individual.vinylo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.domain.Mappers.UserMapper;
import s3.individual.vinylo.domain.dtos.UserDTO;
import s3.individual.vinylo.domain.dtos.UsersDTO;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.domain.User;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public UsersDTO getUsers() {

        List<User> users = userService.getUsers();

        UsersDTO result = UserMapper.toUsersDTO(users);

        return result;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") int id) {

        User u = userService.getUserById(id);

        if (u == null) {
            // If the user is not found, throw this specific exception
            throw new CustomNotFoundException("User record not found");
        }

        UserDTO ud = UserMapper.toUserDTO(u);

        return ResponseEntity.ok(ud);
    }

    @PostMapping()
    public UserDTO createNewUser(@RequestBody @Valid UserDTO newUserDTO) {
        User newUser = UserMapper.toUser(newUserDTO);
        User createdUser = userService.createNewUser(newUser);
        return UserMapper.toUserDTO(createdUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deativateUserById(@PathVariable("id") int id) {
        boolean isDeleted = userService.deativateUserById(id);
        if (isDeleted) {
            return ResponseEntity.ok("User with id " + id + " was successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " was not found.");
        }
    }
}