package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;
import s3.individual.vinylo.serviceimpl.LoginServiceIMPL;
import s3.individual.vinylo.configuration.security.token.AccessTokenEncoder;
import s3.individual.vinylo.configuration.security.token.impl.AccessTokenImpl;
import s3.individual.vinylo.domain.LoginRequest;
import s3.individual.vinylo.domain.LoginResponse;
import s3.individual.vinylo.exceptions.InvalidCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LoginServiceIMPLTest {

    @Mock
    private UserJPARepo userRepoMock;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginServiceIMPL loginService;

    private UserEntity testUserEntity;

    private LoginRequest testLoginRequest;

    @BeforeEach
    void setUp() {

        testUserEntity = UserEntity.builder()
                .id(1)
                .username("Test User")
                .email("TestEmail@gmial.com")
                .password("encodedPassword")
                .role("REGULAR")
                .build();

        testLoginRequest = LoginRequest.builder()
                .username("Test User")
                .password("rawPassword")
                .build();

    }

    @Test
    void testLogin_shouldSucceedAndReturnLoginResponse() {
        // Arrange
        when(userRepoMock.findByUsername(testLoginRequest.getUsername())).thenReturn(Optional.of(testUserEntity));
        when(passwordEncoder.matches(testLoginRequest.getPassword(), testUserEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("accessToken");

        // Act
        LoginResponse response = loginService.login(testLoginRequest);

        // Assert
        assertEquals("accessToken", response.getAccessToken());
        verify(userRepoMock).findByUsername("Test User");
        verify(passwordEncoder).matches("rawPassword", "encodedPassword");
        verify(accessTokenEncoder).encode(any(AccessTokenImpl.class));
    }

    @Test
    void testLogin_shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepoMock.findByUsername("Test User")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> loginService.login(testLoginRequest));
        verify(userRepoMock).findByUsername("Test User");
        verifyNoInteractions(passwordEncoder, accessTokenEncoder);
    }

    @Test
    void testLogin_shouldThrowInvalidCredentialsExceptionWhenPasswordDoesNotMatch() {
        // Arrange
        when(userRepoMock.findByUsername("Test User")).thenReturn(Optional.of(testUserEntity));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> loginService.login(testLoginRequest));
        verify(userRepoMock).findByUsername("Test User");
        verify(passwordEncoder).matches("rawPassword", "encodedPassword");
        verifyNoInteractions(accessTokenEncoder);
    }

    @Test
    void testRefresh_shouldSucceedAndReturnLoginResponse() {
        // Arrange
        when(userRepoMock.findByUsername(testUserEntity.getUsername())).thenReturn(Optional.of(testUserEntity));
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("accessToken");

        // Act
        LoginResponse response = loginService.refresh(testUserEntity.getUsername());

        // Assert
        assertEquals("accessToken", response.getAccessToken());
        verify(userRepoMock).findByUsername("Test User");
        verify(accessTokenEncoder).encode(any(AccessTokenImpl.class));
    }

    @Test
    void testRefresh_shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepoMock.findByUsername("Test User")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> loginService.refresh("Test User"));
        verify(userRepoMock).findByUsername("Test User");
        verifyNoInteractions(accessTokenEncoder);
    }

}
