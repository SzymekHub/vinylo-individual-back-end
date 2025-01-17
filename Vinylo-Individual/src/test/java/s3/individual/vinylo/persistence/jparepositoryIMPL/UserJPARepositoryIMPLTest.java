package s3.individual.vinylo.persistence.jparepositoryimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.jparepository.UserJPARepo;
import s3.individual.vinylo.persistence.jparepositoryimpl.UserJPARepositoryIMPL;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserJPARepositoryIMPLTest {

    @Mock
    private UserJPARepo userJPARepo;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserJPARepositoryIMPL userJPARepoImpl;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        userEntity = UserEntity.builder()
                .id(1)
                .username("TestUser")
                .email("Test@gmail.com")
                .password("Pass")
                .role("REGULAR")
                .build();

        user = User.builder()
                .id(1)
                .username("TestUser")
                .email("Test@gmail.com")
                .password("Pass")
                .role(RoleEnum.REGULAR)
                .build();

        // Inject the mocked EntityManager using reflection
        Field entityManagerField = UserJPARepositoryIMPL.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(userJPARepoImpl, entityManager);
    }

    @Test
    void testSaveUser_SuccessfulSave() {
        // Arrange
        when(userJPARepo.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        User savedUser = userJPARepoImpl.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        verify(userJPARepo, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testSaveUser_UpdateExistingUser() {
        // Arrange
        user.setId(1); // Simulating an update scenario with an existing ID
        userEntity.setId(1); // Corresponding UserEntity
        when(entityManager.merge(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        User updatedUser = userJPARepoImpl.updateUser(user);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(user.getUsername(), updatedUser.getUsername());
        verify(entityManager, times(1)).merge(any(UserEntity.class)); // Verify merge is called
    }

    @Test
    void testSaveUser_NullUser() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userJPARepoImpl.saveUser(null));
        verify(userJPARepo, times(0)).save(any(UserEntity.class)); // Ensure save is not called
        verify(entityManager, times(0)).merge(any(UserEntity.class)); // Ensure merge is not called
    }

    @Test
    void testGetUsers_Success() {
        // Arrange
        when(userJPARepo.findAll()).thenReturn(List.of(userEntity));

        // Act
        List<User> users = userJPARepoImpl.getUsers();

        // Assert
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(user.getUsername(), users.get(0).getUsername());
        verify(userJPARepo, times(1)).findAll();

    }

    @Test
    void testGetUserById_UserExists() {
        // Arrange
        when(userJPARepo.findById(1)).thenReturn(Optional.of(userEntity));

        // Act
        User foundUser = userJPARepoImpl.getUserById(1);

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userJPARepo, times(1)).findById(1);

    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        when(userJPARepo.findById(99)).thenReturn(Optional.empty());

        // Act
        User foundUser = userJPARepoImpl.getUserById(99);

        // Assert
        assertNull(foundUser);
        verify(userJPARepo, times(1)).findById(99);
    }

    @Test
    void testFindByUsername_UserExists() {
        // Arrange
        when(userJPARepo.findByUsername("testUsername")).thenReturn(Optional.of(userEntity));

        // Act
        User foundUser = userJPARepoImpl.findByUsername("testUsername");

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userJPARepo, times(1)).findByUsername("testUsername");

    }

    @Test
    void testFindByUsername_UserNotFound() {
        // Arrange
        when(userJPARepo.findByUsername("NonExistentUser")).thenReturn(Optional.empty());

        // Act
        User foundUser = userJPARepoImpl.findByUsername("NonExistentUser");

        // Assert
        assertNull(foundUser);
        verify(userJPARepo, times(1)).findByUsername("NonExistentUser");
    }

    @Test
    void testDeactivateUserById_UserExists() {
        // Arrange
        when(userJPARepo.existsById(1)).thenReturn(true);

        // Act
        boolean isDeactivated = userJPARepoImpl.deativateUserById(1);

        // Assert
        assertTrue(isDeactivated);
        verify(userJPARepo, times(1)).deleteById(1);

    }

    @Test
    void testDeactivateUserById_NotFound() {
        // Arrange
        when(userJPARepo.existsById(1)).thenReturn(false);

        // Act
        boolean isDeactivated = userJPARepoImpl.deativateUserById(1);

        // Assert
        assertFalse(isDeactivated);
        verify(userJPARepo, times(0)).deleteById(1);
    }

}
