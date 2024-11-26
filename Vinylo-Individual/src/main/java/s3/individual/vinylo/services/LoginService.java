package s3.individual.vinylo.services;

import s3.individual.vinylo.domain.LoginRequest;
import s3.individual.vinylo.domain.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest);
}
