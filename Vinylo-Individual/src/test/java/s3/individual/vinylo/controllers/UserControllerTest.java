package s3.individual.vinylo.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.services.ProfileService;
import s3.individual.vinylo.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private ProfileService profileService;

        @Test
        void testAddUser_ShouldCreateAndReturn201_WhenRequestValid() throws Exception {
                // Arrange
                User createdUser = User.builder()
                                .id(100)
                                .username("Username Test")
                                .email("UserTest@gmail.com")
                                .password("UserTestPass")
                                .role(RoleEnum.REGULAR)
                                .build();
                // Act
                when(userService.saveUser(eq(null), any(User.class))).thenReturn(createdUser);
                when(profileService.saveProfile(eq(null), any(Profile.class)))
                                .thenReturn(new Profile(0, createdUser, "", 0));

                // Assert
                mockMvc.perform(post("/users")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content("""
                                                            {
                                                "username": "Username Test",
                                                "email" : "UserTest@gmail.com",
                                                "password" : "UserTestPass",
                                                "role" : "REGULAR"

                                                            }
                                                        """))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(content().string("User created successfully"));

                verify(userService).saveUser(eq(null), any(User.class));
                verify(profileService).saveProfile(eq(null), any(Profile.class));
        }

        @Test
        void testAddUser_ShouldCreateAndReturn400_WhenMissingFields() throws Exception {

                // Act and Assert
                mockMvc.perform(post("/users")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content("""
                                                            {
                                                "username": "Username Test",
                                                "email" : "",
                                                "password" : "UserTestPass",
                                                "role" : "REGULAR"

                                                            }
                                                        """))
                                .andDo(print())
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(userService);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeactivateUserById_shouldReturn201_WhenRequestValid() throws Exception {
                // Arrange
                int userId = 100;
                when(userService.deativateUserById(userId)).thenReturn(true);

                // Act and Assert
                mockMvc.perform(delete("/users/{id}", userId))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("User with id " + userId + " was successfully deleted."));

                verify(userService).deativateUserById(userId);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeativateUserById_ShouldReturn404_WhenUserNotFound() throws Exception {
                // Arrange
                int userId = 200;
                when(userService.deativateUserById(userId)).thenReturn(false);

                // Act and Assert
                mockMvc.perform(delete("/users/{id}", userId))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("User with id " + userId + " was not found."));

                verify(userService).deativateUserById(userId);
        }

        @Test
        void testGetUsers_shouldReturn200RespondWithUsersArray() throws Exception {
                // Arrange
                List<User> users = List.of(
                                User.builder()
                                                .id(1)
                                                .username("UserName1")
                                                .email("Email1@gmail.com")
                                                .password("Password1")
                                                .role(RoleEnum.REGULAR)
                                                .build(),
                                User.builder()
                                                .id(2)
                                                .username("UserName2")
                                                .email("Email2@gmail.com")
                                                .password("Password2")
                                                .role(RoleEnum.PREMIUM)
                                                .build());

                // Act
                when(userService.getUsers()).thenReturn(users);

                // Assert
                mockMvc.perform(get("/users"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.users[0].id").value(1))
                                .andExpect(jsonPath("$.users[0].username").value("UserName1"))
                                .andExpect(jsonPath("$.users[1].id").value(2))
                                .andExpect(jsonPath("$.users[1].username").value("UserName2"));

                verify(userService).getUsers();
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testGetUser_shouldReturn200RespondWithUserByID() throws Exception {
                // Arrange
                int userId = 1;
                User user = User.builder()
                                .id(userId)
                                .username("UserName1")
                                .email("Email1@gmail.com")
                                .password("Password1")
                                .role(RoleEnum.REGULAR)
                                .build();

                when(userService.getUserById(userId)).thenReturn(user);

                // Act and Assert
                mockMvc.perform(get("/users/{id}", userId))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.id").value(userId))
                                .andExpect(jsonPath("$.username").value("UserName1"))
                                .andExpect(jsonPath("$.email").value("Email1@gmail.com"))
                                .andExpect(jsonPath("$.role").value(RoleEnum.REGULAR.toString()));

                verify(userService).getUserById(userId);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testGetUser_shouldReturn404_WhenUserNotFound() throws Exception {
                // Arrange
                int userId = 999;
                when(userService.getUserById(userId)).thenReturn(null);

                // Act and Assert
                mockMvc.perform(get("/users/{id}", userId))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("User record not found"));

                verify(userService).getUserById(userId);
        }
}
