package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import s3.individual.vinylo.persistence.UserRepo;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.serviceimpl.UserServiceIMPL;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceIMPLTest {

    @Mock
    private UserRepo userRepoMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    private UserServiceIMPL userService;

    private User createUser(Integer id, String username, String email, String password, RoleEnum role) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    @Test
    void testSaveUser_shouldSaveAndReturnNewUser() {
        // Arrange
        User newUser = createUser(null, "Username1", "UseerName1@gmail.com", "User1Password", RoleEnum.REGULAR);
        User savedUser = createUser(2, "Username1", "UseerName1@gmail.com", "EncodedPassword", RoleEnum.REGULAR);

        when(passwordEncoderMock.encode("User1Password")).thenReturn("EncodedPassword");
        when(userRepoMock.saveUser(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(newUser);

        // Assert
        assertEquals(savedUser, result);
        verify(passwordEncoderMock).encode("User1Password");
        verify(userRepoMock).saveUser(any(User.class));
    }

    @Test
    void testSaveUser_shouldUpdateExistingUser() {
        // Arrange
        int userId = 2;

        User existingUser = createUser(userId,
                "Username1",
                "UseerName1@gmail.com",
                "User1Password",
                RoleEnum.REGULAR);

        User updatedUser = createUser(userId,
                "Crazy Username",
                "UseerName2@gmail.com",
                "User2Password",
                RoleEnum.REGULAR);

        User updatedUserEntity = createUser(userId,
                "Crazy Username",
                "UseerName2@gmail.com",
                "EncodedPassword",
                RoleEnum.REGULAR);

        when(userRepoMock.getUserById(userId)).thenReturn(existingUser);
        when(userRepoMock.updateUser(existingUser)).thenReturn(updatedUserEntity);

        // Act
        User result = userService.updateUser(updatedUser);

        // Assert
        assertEquals("Crazy Username", result.getUsername());

        verify(userRepoMock).updateUser(existingUser);
        verify(userRepoMock).getUserById(userId);
    }

    @Test
    void testSaveUser_shouldThrowExceptionWhenUserNotFoundForUpdate() {
        // Arrange
        int userId = 2;

        User updatedUser = createUser(userId,
                "Crazy Username",
                "UseerName2@gmail.com",
                "User2Password",
                RoleEnum.REGULAR);

        when(userRepoMock.getUserById(userId)).thenReturn(null);

        // Act and Assert
        assertThrows(CustomInternalServerErrorException.class, () -> {
            userService.updateUser(updatedUser); // Should be updateUser, not createUser
        });
    }

    @Test
    void testFindByUsername_shouldReturnUserWithGivenUsername() {
        // Arrange
        String username = "RubyDaCherry";

        User expectedUser = createUser(666,
                "RubyDaCherry",
                "RubyDaCherry@gmail.com",
                "YoungPlaque666",
                RoleEnum.PREMIUM);

        when(userRepoMock.findByUsername(username)).thenReturn(expectedUser);

        // Act
        User actualUser = userService.findByUsername(username);

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepoMock).findByUsername(username);
    }

    @Test
    void testGetUserById_shouldReturnUserWithGivenId() {
        // Arrange
        int userId = 1;

        User expectedUser = createUser(userId,
                "Johnny",
                "JohnnyTheBoss@gmail.com",
                "JOhnny1234",
                RoleEnum.PREMIUM);

        when(userRepoMock.getUserById(userId)).thenReturn(expectedUser);

        // Act
        User actualUser = userService.getUserById(userId);

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepoMock).getUserById(userId);

    }

    @Test
    void testGetUsers_shouldReturnAllUsers() {
        // Arrange
        User user = createUser(666,
                "Johnny",
                "JohnnyTheBoss@gmail.com",
                "JOhnny1234",
                RoleEnum.PREMIUM);

        User user2 = createUser(888,
                "Pablo",
                "PabloTheBoss@gmail.com",
                "Pablo69",
                RoleEnum.REGULAR);

        when(userRepoMock.getUsers()).thenReturn(List.of(user, user2));

        // Act
        List<User> actualUsers = userService.getUsers();

        // Assert
        assertEquals(List.of(user, user2), actualUsers);
        verify(userRepoMock).getUsers();

    }

    @Test
    void testDeativateUserById_ShouldReturnTrueWhenUserIsDeactivated() {
        // Arrange
        int userId = 1;

        when(userRepoMock.deativateUserById(userId)).thenReturn(true);

        // Act
        boolean isDeleted = userService.deativateUserById(userId);

        // Assert
        assertEquals(true, isDeleted);
        verify(userRepoMock).deativateUserById(userId);
    }

}
