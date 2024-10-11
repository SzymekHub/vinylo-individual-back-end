package s3.individual.vinylo.Models.controllers;

import org.springframework.http.ResponseEntity;
import s3.individual.vinylo.Models.dtos.UserDTO;
import s3.individual.vinylo.Models.dtos.UsersDTO;

public interface UserController {

    UsersDTO getUsers();

    ResponseEntity<?> getUser(int id);

    UserDTO createNewUser(UserDTO newUserDTO);

    ResponseEntity<String> deativateUserById(int id);

}