package s3.individual.vinylo.serviceimpl;

import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.configuration.security.token.AccessTokenEncoder;
import s3.individual.vinylo.configuration.security.token.impl.AccessTokenImpl;
import s3.individual.vinylo.domain.LoginRequest;
import s3.individual.vinylo.domain.LoginResponse;
import s3.individual.vinylo.exceptions.InvalidCredentialsException;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;
import s3.individual.vinylo.services.LoginService;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginServiceIMPL implements LoginService {

    private final UserJPARepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<UserEntity> optionalUser = userRepo.findByUsername(loginRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        UserEntity user = optionalUser.get();

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        int id = user.getId();
        String role = user.getRole().toString(); // Convert the single RoleEnum to a string

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getUsername(), id, List.of(role)));
    }
}
