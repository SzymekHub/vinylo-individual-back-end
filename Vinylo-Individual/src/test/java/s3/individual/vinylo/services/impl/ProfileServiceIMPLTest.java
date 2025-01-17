package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.domain.dtos.ProfileAndUserDTO;
import s3.individual.vinylo.domain.dtos.ProfileDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.exceptions.DuplicateItemException;
import s3.individual.vinylo.persistence.ProfileRepo;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.serviceimpl.ProfileServiceIMPL;
import s3.individual.vinylo.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceIMPLTest {

    @Mock
    private ProfileRepo profileRepoMock;

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private ProfileServiceIMPL profileService;

    private Profile testProfile;
    private User testUser;
    private ProfileDTO testProfileDTO;
    private UserEntity testUserEntity;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@gmail.com");
        testUser.setPassword("testUser");
        testUser.setRole(RoleEnum.REGULAR);

        testProfile = new Profile();
        testProfile.setId(1);
        testProfile.setUser(testUser);
        testProfile.setBio("I am USER 1");
        testProfile.setBalance(5000);

        testProfileDTO = new ProfileDTO();
        testProfileDTO.setId(1);
        testProfileDTO.setUser_id(1);
        testProfileDTO.setBio("I am USER 1");
        testProfileDTO.setBalance(5000);

        testUserEntity = UserEntity.builder()
                .id(1)
                .username("testUser")
                .email("testUser@gmail.com")
                .password("encodedPassword")
                .role("REGULAR")
                .build();
    }

    @Test
    void testCreateProfile_ShouldSaveAndReturnNewProfile() {
        // Arrange
        when(profileRepoMock.findByBioAndUser(testProfile.getBio(), testProfile.getUser().getId())).thenReturn(null);
        when(profileRepoMock.saveProfile(testProfile)).thenReturn(testProfile);

        // Act
        Profile result = profileService.createProfile(testProfile);

        // Assert
        assertEquals(testProfile, result);
        verify(profileRepoMock).saveProfile(testProfile);
    }

    @Test
    void testCreateProfile_ShouldThrowDuplicateItemException() {
        // Arrange
        when(profileRepoMock.findByBioAndUser(testProfile.getBio(), testProfile.getUser().getId()))
                .thenReturn(testProfile);

        // Act & Assert
        assertThrows(DuplicateItemException.class, () -> profileService.createProfile(testProfile));
        verify(profileRepoMock, never()).saveProfile(testProfile);
    }

    @Test
    void testUpdateProfile_ShouldUpdateAndReturnProfile() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(testProfile);
        when(profileRepoMock.saveProfile(testProfile)).thenReturn(testProfile);

        // Act
        Profile result = profileService.updateProfile(testProfile.getUser().getId(), testProfileDTO);

        // Assert
        assertEquals(testProfile, result);
        verify(profileRepoMock).saveProfile(testProfile);
    }

    @Test
    void testUpdateProfile_ShouldThrowCustomNotFoundException() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class,
                () -> profileService.updateProfile(testProfile.getUser().getId(), testProfileDTO));
        verify(profileRepoMock, never()).saveProfile(testProfile);
    }

    @Test
    void testUpgradeToPremium_ShouldUpgradeAndReturnProfile() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(testProfile);
        when(userServiceMock.getUserById(testProfile.getUser().getId())).thenReturn(testUser);
        when(profileRepoMock.saveProfile(testProfile)).thenReturn(testProfile);

        // Act
        Profile result = profileService.upgradeToPremium(testProfile.getUser().getId());

        // Assert
        assertEquals(testProfile, result);
        verify(profileRepoMock).saveProfile(testProfile);
        verify(userServiceMock).updateUser(testUser);
    }

    @Test
    void testUpgradeToPremium_ShouldThrowCustomNotFoundExceptionForProfile() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class,
                () -> profileService.upgradeToPremium(testProfile.getUser().getId()));
        verify(profileRepoMock, never()).saveProfile(testProfile);
        verify(userServiceMock, never()).updateUser(testUser);
    }

    @Test
    void testUpgradeToPremium_ShouldThrowCustomNotFoundExceptionForUser() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(testProfile);
        when(userServiceMock.getUserById(testProfile.getUser().getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class,
                () -> profileService.upgradeToPremium(testProfile.getUser().getId()));
        verify(profileRepoMock, never()).saveProfile(testProfile);
        verify(userServiceMock, never()).updateUser(testUser);
    }

    @Test
    void testGetProfileAndUserById_ShouldReturnProfileAndUserDTO() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(testProfile);
        when(profileRepoMock.getUserByUserId(testProfile.getUser().getId())).thenReturn(testUserEntity);

        // Act
        ProfileAndUserDTO result = profileService.getProfileAndUserById(testProfile.getUser().getId());

        // Assert
        assertNotNull(result);
        assertEquals(testProfileDTO.getBio(), result.getProfile().getBio());
        assertEquals(testUserEntity.getUsername(), result.getUser().getUsername());
        verify(profileRepoMock).findByUserId(testProfile.getUser().getId());
        verify(profileRepoMock).getUserByUserId(testProfile.getUser().getId());
    }

    @Test
    void testGetProfileAndUserById_ShouldThrowCustomNotFoundExceptionForProfile() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class,
                () -> profileService.getProfileAndUserById(testProfile.getUser().getId()));
        verify(profileRepoMock, never()).getUserByUserId(testProfile.getUser().getId());
    }

    @Test
    void testGetProfileAndUserById_ShouldThrowCustomNotFoundExceptionForUser() {
        // Arrange
        when(profileRepoMock.findByUserId(testProfile.getUser().getId())).thenReturn(testProfile);
        when(profileRepoMock.getUserByUserId(testProfile.getUser().getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class,
                () -> profileService.getProfileAndUserById(testProfile.getUser().getId()));
        verify(profileRepoMock).findByUserId(testProfile.getUser().getId());
    }
}
