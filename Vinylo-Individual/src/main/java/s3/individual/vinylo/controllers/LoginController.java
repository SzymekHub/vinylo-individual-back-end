package s3.individual.vinylo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.domain.LoginRequest;
import s3.individual.vinylo.domain.LoginResponse;
import s3.individual.vinylo.services.LoginService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = loginService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully logged in " + loginResponse);
    }
}
