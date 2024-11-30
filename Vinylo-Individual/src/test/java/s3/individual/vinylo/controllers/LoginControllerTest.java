package s3.individual.vinylo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import s3.individual.vinylo.domain.LoginRequest;
import s3.individual.vinylo.domain.LoginResponse;
import s3.individual.vinylo.exceptions.InvalidCredentialsException;
import s3.individual.vinylo.services.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Test
    void testLogin_shouldReturn201WithLoginResponse() throws Exception {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .username("Test User")
                .password("password123")
                .build();
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("valid-access-token")
                .build();

        when(loginService.login(loginRequest)).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                            {
                                "username": "Test User",
                                "password": "password123"
                            }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("valid-access-token"));

        verify(loginService).login(any(LoginRequest.class));
    }

    @Test
    void testLogin_shouldReturn401_WhenInvalidCredentials() throws Exception {
        // Arrange
        when(loginService.login(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException());

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                            {
                                "username": "Test User",
                                "password": "wrongpassword"
                            }
                        """))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(loginService).login(any(LoginRequest.class));
    }

    @Test
    void testLogin_shouldReturn400_WhenMissingFields() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                            {
                                "username": "Test User"
                            }
                        """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loginService);
    }

    @Test
    @WithMockUser(roles = "REGULAR")
    void testLogin_withAuthenticatedUser_shouldReturn201WithLoginResponse() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("Test User")
                .password("password123")
                .build();
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("valid-access-token")
                .build();

        when(loginService.login(loginRequest)).thenReturn(loginResponse);

        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                            {
                                "username": "Test User",
                                "password": "password123"
                            }
                        """))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(loginService).login(loginRequest);

    }
}
