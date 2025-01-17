package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.exceptions.DuplicateItemException;
import s3.individual.vinylo.persistence.VinylCollectionRepo;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.serviceimpl.VinylCollectionServiceIMPL;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VinylCollectionServiceIMPLTest {

    @Mock
    private VinylCollectionRepo vinylCollectionRepoMock;

    @InjectMocks
    private VinylCollectionServiceIMPL vinylCollectionService;

    private User testUser;
    private Vinyl testVinyl;
    private VinylCollection testVinylCollection;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@gmail.com");
        testUser.setPassword("testUser");
        testUser.setRole(RoleEnum.REGULAR);

        testVinyl = new Vinyl();
        testVinyl.setId(1);
        testVinyl.setVinylType(VinylTypeEnum.LP_12_INCH);
        testVinyl.setSpeed(SpeedEnum.RPM_45);
        testVinyl.setTitle("Test Vinyl");
        testVinyl.setDescription("Test Desc");
        testVinyl.setState(StateEnum.ORIGINAL);
        testVinyl.setColor(VinylColorEnum.WHITE);
        testVinyl.setIsReleased(true);
        testVinyl.setSpotifyAlbumId("afwqf123214");

        testVinylCollection = new VinylCollection();
        testVinylCollection.setId(1);
        testVinylCollection.setUser(testUser);
        testVinylCollection.setVinyl(testVinyl);
    }

    @Test
    void testAddVinylToCollection_ShouldSaveAndReturnNewVinylCollection() {
        // Arrange
        when(vinylCollectionRepoMock.findByUserAndVinyl(testUser.getId(), testVinyl.getId())).thenReturn(null);
        when(vinylCollectionRepoMock.save(testVinylCollection)).thenReturn(testVinylCollection);

        // Act
        VinylCollection result = vinylCollectionService.addVinylToCollection(null, testVinylCollection);

        // Assert
        assertEquals(testVinylCollection, result);
        verify(vinylCollectionRepoMock).save(testVinylCollection);
    }

    @Test
    void testAddVinylToCollection_ShouldThrowDuplicateItemException() {
        // Arrange
        when(vinylCollectionRepoMock.findByUserAndVinyl(testUser.getId(), testVinyl.getId()))
                .thenReturn(testVinylCollection);

        // Act & Assert
        assertThrows(DuplicateItemException.class,
                () -> vinylCollectionService.addVinylToCollection(null, testVinylCollection));
        verify(vinylCollectionRepoMock, never()).save(testVinylCollection);
    }

    @Test
    void testAddVinylToCollection_ShouldUpdateExistingVinylCollection() {
        // Arrange
        when(vinylCollectionRepoMock.findById(testVinylCollection.getId())).thenReturn(testVinylCollection);
        when(vinylCollectionRepoMock.findByUserAndVinyl(testUser.getId(), testVinyl.getId())).thenReturn(null);
        when(vinylCollectionRepoMock.save(testVinylCollection)).thenReturn(testVinylCollection);

        // Act
        VinylCollection result = vinylCollectionService.addVinylToCollection(testVinylCollection.getId(),
                testVinylCollection);

        // Assert
        assertEquals(testVinylCollection, result);
        verify(vinylCollectionRepoMock).save(testVinylCollection);
    }

    @Test
    void testAddVinylToCollection_ShouldThrowCustomNotFoundException() {
        // Arrange
        when(vinylCollectionRepoMock.findById(testVinylCollection.getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class,
                () -> vinylCollectionService.addVinylToCollection(testVinylCollection.getId(), testVinylCollection));
        verify(vinylCollectionRepoMock, never()).save(testVinylCollection);
    }

    @Test
    void testGetAllCollections_ShouldReturnAllVinylCollections() {
        // Arrange
        VinylCollection vinylCollection1 = new VinylCollection();
        vinylCollection1.setId(2);
        vinylCollection1.setUser(testUser);
        vinylCollection1.setVinyl(testVinyl);

        when(vinylCollectionRepoMock.findAll()).thenReturn(List.of(testVinylCollection, vinylCollection1));

        // Act
        List<VinylCollection> result = vinylCollectionService.getAllCollections();

        // Assert
        assertEquals(List.of(testVinylCollection, vinylCollection1), result);
        verify(vinylCollectionRepoMock).findAll();
    }

    @Test
    void testGetCollectionById_ShouldReturnVinylCollectionWithGivenId() {
        // Arrange
        when(vinylCollectionRepoMock.findById(testVinylCollection.getId())).thenReturn(testVinylCollection);

        // Act
        VinylCollection result = vinylCollectionService.getCollectionById(testVinylCollection.getId());

        // Assert
        assertEquals(testVinylCollection, result);
        verify(vinylCollectionRepoMock).findById(testVinylCollection.getId());
    }

    @Test
    void testGetCollectionByUserId_ShouldReturnVinylCollectionsForUser() {
        // Arrange
        VinylEntity testVinylEntity = new VinylEntity();
        testVinylEntity.setId(testVinyl.getId());
        testVinylEntity.setVinylType(testVinyl.getVinylType().toString());
        testVinylEntity.setSpeed(testVinyl.getSpeed().toString());
        testVinylEntity.setTitle(testVinyl.getTitle());
        testVinylEntity.setDescription(testVinyl.getDescription());
        testVinylEntity.setState(testVinyl.getState().toString());
        testVinylEntity.setColor(testVinyl.getColor().toString());
        testVinylEntity.setIsReleased(testVinyl.getIsReleased());
        testVinylEntity.setSpotifyAlbumId(testVinyl.getSpotifyAlbumId());

        when(vinylCollectionRepoMock.findByUserId(testUser.getId())).thenReturn(List.of(testVinylEntity));

        // Act
        List<VinylEntity> result = vinylCollectionService.getCollectionByUserId(testUser.getId());

        // Assert
        assertEquals(List.of(testVinylEntity), result);
        verify(vinylCollectionRepoMock).findByUserId(testUser.getId());
    }

    @Test
    void testGetByUserIdAndVinylId_ShouldReturnVinylEntity() {
        // Arrange
        VinylEntity testVinylEntity = new VinylEntity();
        testVinylEntity.setId(testVinyl.getId());
        testVinylEntity.setVinylType(testVinyl.getVinylType().toString());
        testVinylEntity.setSpeed(testVinyl.getSpeed().toString());
        testVinylEntity.setTitle(testVinyl.getTitle().toString());
        testVinylEntity.setDescription(testVinyl.getDescription());
        testVinylEntity.setState(testVinyl.getState().toString());
        testVinylEntity.setColor(testVinyl.getColor().toString());
        testVinylEntity.setIsReleased(testVinyl.getIsReleased());
        testVinylEntity.setSpotifyAlbumId(testVinyl.getSpotifyAlbumId());

        when(vinylCollectionRepoMock.findByUserIdAndVinylId(testUser.getId(), testVinyl.getId()))
                .thenReturn(testVinylEntity);

        // Act
        VinylEntity result = vinylCollectionService.getByUserIdAndVinylId(testUser.getId(), testVinyl.getId());

        // Assert
        assertEquals(testVinylEntity, result);
        verify(vinylCollectionRepoMock).findByUserIdAndVinylId(testUser.getId(), testVinyl.getId());
    }

    @Test
    void testDeleteCollection_ShouldReturnTrueWhenVinylCollectionIsDeleted() {
        // Arrange
        when(vinylCollectionRepoMock.deleteByVinylIdAndUserId(testVinyl.getId(), testUser.getId())).thenReturn(true);

        // Act
        boolean result = vinylCollectionService.deleteCollection(testVinyl.getId(), testUser.getId());

        // Assert
        assertTrue(result);
        verify(vinylCollectionRepoMock).deleteByVinylIdAndUserId(testVinyl.getId(), testUser.getId());
    }
}
