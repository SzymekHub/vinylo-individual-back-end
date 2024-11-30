package s3.individual.vinylo.persistence.jparepositoryIMPL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.persistence.entity.ArtistEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.jparepository.ArtistJPARepo;
import s3.individual.vinylo.persistence.jparepository.VinylJPARepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VinylJPARepoIMPLTest {

    @Mock
    private VinylJPARepo vinylJPARepo;

    @Mock
    private ArtistJPARepo artistJPARepo;

    @InjectMocks
    private VinylJPARepoIMPL vinylJPARepoIMPL;

    @Mock
    private EntityManager entityManager; // Mock EntityManager

    private VinylEntity vinylEntity;

    private ArtistEntity artistEntity;

    private Artist artist;

    private Vinyl vinyl;

    @BeforeEach
    void setUp() {

        artistEntity = ArtistEntity.builder()
                .id(1)
                .name("Test artist")
                .bio("Test bio")
                .build();
        artist = Artist.builder()
                .id(1)
                .name("Test artist")
                .bio("Test bio")
                .build();

        vinylEntity = VinylEntity.builder()
                .id(1)
                .vinylType("EP")
                .title("Test Vinyl")
                .description("Test Desc")
                .isReleased(false)
                .artist(artistEntity)
                .build();

        vinyl = Vinyl.builder()
                .id(1)
                .vinylType("EP")
                .title("Test Vinyl")
                .description("Test Description")
                .isReleased(false)
                .artist(artist)
                .build();
    }

    // !! to ve fixed
    // @Test
    // void saveVinyl_ShouldReturnSavedVinyl() {
    // // Arrange
    // when(artistJPARepo.findById(artist.getId())).thenReturn(Optional.of(artistEntity));
    // // Mock artist lookup
    // when(vinylJPARepo.save(any(VinylEntity.class))).thenReturn(vinylEntity); //
    // Mock save

    // // Act
    // Vinyl savedVinyl = vinylJPARepoIMPL.saveVinyl(vinyl);

    // // Assert
    // assertNotNull(savedVinyl);
    // assertEquals(vinyl.getTitle(), savedVinyl.getTitle());
    // verify(vinylJPARepo, times(1)).save(any(VinylEntity.class));
    // verify(artistJPARepo, times(1)).findById(artist.getId()); // Ensure the
    // artist was looked up
    // }

    // @Test
    // void saveVinyl_ShouldHandleNullArtistGracefully() {
    // // Arrange
    // vinyl.setArtist(null); // Set the artist to null

    // when(vinylJPARepo.save(any(VinylEntity.class))).thenReturn(vinylEntity); //
    // Mock save

    // // Act
    // Vinyl savedVinyl = vinylJPARepoIMPL.saveVinyl(vinyl);

    // // Assert
    // assertNotNull(savedVinyl);
    // assertEquals(vinyl.getTitle(), savedVinyl.getTitle());
    // verify(vinylJPARepo, times(1)).save(any(VinylEntity.class));
    // verify(artistJPARepo, times(0)).findById(any()); // Ensure the artist lookup
    // was not attempted
    // }

    @Test
    void getAllVinyls_ShouldReturnListOfVinyls() {
        // Arrange
        when(vinylJPARepo.findAll()).thenReturn(List.of(vinylEntity));

        // Act
        List<Vinyl> vinyls = vinylJPARepoIMPL.getVinyls();

        // Assert
        assertNotNull(vinyls);
        assertEquals(1, vinyls.size());
        assertEquals(vinyl.getTitle(), vinyls.get(0).getTitle());
        verify(vinylJPARepo, times(1)).findAll();
    }

    @Test
    void getVinylById_ShouldReturnVinylWhenExists() {
        // Arrange
        when(vinylJPARepo.findById(1)).thenReturn(Optional.of(vinylEntity));

        // Act
        Vinyl foundVinyl = vinylJPARepoIMPL.getVinylById(1);

        // Assert
        assertNotNull(foundVinyl);
        assertEquals(vinyl.getTitle(), foundVinyl.getTitle());
        verify(vinylJPARepo, times(1)).findById(1);
    }

    @Test
    void getVinylById_ShouldReturnNullWhenNotFound() {
        // Arrange
        when(vinylJPARepo.findById(2)).thenReturn(Optional.empty());

        // Act
        Vinyl foundVinyl = vinylJPARepoIMPL.getVinylById(2);

        // Assert
        assertNull(foundVinyl);
        verify(vinylJPARepo, times(1)).findById(2);
    }

    @Test
    void deleteVinylById_ShouldReturnTrueWhenDeleted() {
        // Arrange
        when(vinylJPARepo.existsById(1)).thenReturn(true);

        // Act
        boolean result = vinylJPARepoIMPL.deleteVinylById(1);

        // Assert
        assertTrue(result);
        verify(vinylJPARepo, times(1)).deleteById(1);
    }

    @Test
    void deleteVinylById_ShouldReturnFalseWhenNotFound() {
        // Arrange
        when(vinylJPARepo.existsById(2)).thenReturn(false);

        // Act
        boolean result = vinylJPARepoIMPL.deleteVinylById(2);

        // Assert
        assertFalse(result);
        verify(vinylJPARepo, times(0)).deleteById(2);
    }

}
