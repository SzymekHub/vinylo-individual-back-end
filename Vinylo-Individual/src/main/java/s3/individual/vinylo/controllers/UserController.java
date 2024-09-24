package s3.individual.vinylo.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import s3.individual.vinylo.persistence.dtos.UserDTO;
import s3.individual.vinylo.persistence.dtos.UsersDTO;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.services.domain.User;


@RestController
@RequestMapping("/users") 
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public UsersDTO getUsers() {
        
        ArrayList<User> users = userService.getUsers();

        UsersDTO result = new UsersDTO();

        for (User u: users)
        {
            UserDTO ud = new UserDTO();
            ud.id = u.getId();
            ud.username = u.getUsername();
            ud.email = u.getEmail();
            ud.password = u.getPassword();
            ud.isPremium = u.getIsPremium();
            result.users.add(ud);
        }

        return result;
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {

         User u = userService.getUserById(id);

         UserDTO ud = new UserDTO();
         ud.id = u.getId();
         ud.username = u.getUsername();
         ud.email = u.getEmail();
         ud.password = u.getPassword();
         ud.isPremium = u.getIsPremium();
         
         return ud;
    }

    @PostMapping()
    public User createNewUser(@RequestBody User newUser) {
        return userService.createNewUser(newUser);
    }

    @DeleteMapping("/{id}")
    public String deativateUserById(@PathVariable int id) {  
        boolean isDeleted = userService.deativateUserById(id);
        if (isDeleted) {
            return "User with id " + id + " was successfully deleted.";
        } else {
            return "User with id " + id + " was not found.";
        }
    }
}