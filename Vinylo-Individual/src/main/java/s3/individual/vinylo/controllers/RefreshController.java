package s3.individual.vinylo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import s3.individual.vinylo.configuration.security.auth.RequestAuthenticatedUserProvider;
import s3.individual.vinylo.configuration.security.token.AccessToken;
import s3.individual.vinylo.domain.LoginResponse;
import s3.individual.vinylo.services.LoginService;

@RestController
@RequestMapping("/refresh-token")
@RequiredArgsConstructor
public class RefreshController {

    private final LoginService loginService;
    private final RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;

    @PostMapping
    public ResponseEntity<LoginResponse> refreshToken() {
        // Retrieves the current user from the token
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Generates a new token using the username extracted from the existing token
        LoginResponse loginResponse = loginService.refresh(accessToken.getUsername());

        return ResponseEntity.ok(loginResponse);
    }
}