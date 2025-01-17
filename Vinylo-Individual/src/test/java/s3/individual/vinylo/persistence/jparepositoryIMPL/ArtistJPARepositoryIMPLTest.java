package s3.individual.vinylo.persistence.jparepositoryimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.persistence.entity.ArtistEntity;
import s3.individual.vinylo.persistence.jparepository.ArtistJPARepo;
import s3.individual.vinylo.persistence.jparepositoryimpl.ArtistJPARepositoryIMPL;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistJPARepositoryIMPLTest {
    @Mock
    private ArtistJPARepo artistJPARepo;

    @InjectMocks
    private ArtistJPARepositoryIMPL artistJPARepoImpl;

    private ArtistEntity artistEntity;
    private Artist artist;

    @BeforeEach
    void setUp() {
        artistEntity = ArtistEntity.builder()
                .id(1)
                .name("Test Artist")
                .bio("Test Bio")
                .build();

        artist = Artist.builder()
                .id(1)
                .name("Test Artist")
                .bio("Test Bio")
                .build();
    }

    @Test
    void testSaveArtist_SuccessfulSave() {
        // Arrange
        when(artistJPARepo.save(any(ArtistEntity.class))).thenReturn(artistEntity);

        // Act
        Artist savedArtist = artistJPARepoImpl.saveArtist(artist);

        // Assert
        assertNotNull(savedArtist);
        assertEquals(artist.getName(), savedArtist.getName());
        verify(artistJPARepo, times(1)).save(any(ArtistEntity.class));
    }

    @SuppressWarnings("null")
    @Test
    void testSaveArtist_NullArtist() {
        // Arrange
        when(artistJPARepo.save(null)).thenThrow(new IllegalArgumentException("ArtistEntity cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> artistJPARepoImpl.saveArtist(null));
        verify(artistJPARepo, times(1)).save(null);
    }

    @Test
    void testGetArtists_Success() {
        // Arrange
        when(artistJPARepo.findAll()).thenReturn(List.of(artistEntity));

        // Act
        List<Artist> artists = artistJPARepoImpl.getArtists();

        // Assert
        assertFalse(artists.isEmpty());
        assertEquals(1, artists.size());
        assertEquals(artist.getName(), artists.get(0).getName());
        verify(artistJPARepo, times(1)).findAll();
    }

    @Test
    void testGetArtists_EmptyList() {
        // Arrange
        when(artistJPARepo.findAll()).thenReturn(List.of());

        // Act
        List<Artist> artists = artistJPARepoImpl.getArtists();

        // Assert
        assertTrue(artists.isEmpty());
        verify(artistJPARepo, times(1)).findAll();
    }

    @Test
    void testGetArtistById_ArtistExists() {
        // Arrange
        when(artistJPARepo.findById(1)).thenReturn(Optional.of(artistEntity));

        // Act
        Artist foundArtist = artistJPARepoImpl.getArtistById(1);

        // Assert
        assertNotNull(foundArtist);
        assertEquals(artist.getName(), foundArtist.getName());
        verify(artistJPARepo, times(1)).findById(1);
    }

    @Test
    void testGetArtistById_ArtistNotFound() {
        // Arrange
        when(artistJPARepo.findById(99)).thenReturn(Optional.empty());

        // Act
        Artist foundArtist = artistJPARepoImpl.getArtistById(99);

        // Assert
        assertNull(foundArtist);
        verify(artistJPARepo, times(1)).findById(99);
    }

    @Test
    void testGetArtistByName_ArtistExists() {
        // Arrange
        when(artistJPARepo.findByName("Test Artist")).thenReturn(Optional.of(artistEntity));

        // Act
        Artist foundArtist = artistJPARepoImpl.getArtistByName("Test Artist");

        // Assert
        assertNotNull(foundArtist);
        assertEquals(artist.getName(), foundArtist.getName());
        verify(artistJPARepo, times(1)).findByName("Test Artist");
    }

    @Test
    void testGetArtistByName_ArtistNotFound() {
        // Arrange
        when(artistJPARepo.findByName("NonExistent Artist")).thenReturn(Optional.empty());

        // Act
        Artist foundArtist = artistJPARepoImpl.getArtistByName("NonExistent Artist");

        // Assert
        assertNull(foundArtist);
        verify(artistJPARepo, times(1)).findByName("NonExistent Artist");
    }

    @Test
    void testDeactivateArtistById_ArtistExists() {
        // Arrange
        when(artistJPARepo.existsById(1)).thenReturn(true);

        // Act
        boolean isDeactivated = artistJPARepoImpl.deactivateArtistById(1);

        // Assert
        assertTrue(isDeactivated);
        verify(artistJPARepo, times(1)).deleteById(1);
    }

    @Test
    void testDeactivateArtistById_ArtistNotFound() {
        // Arrange
        when(artistJPARepo.existsById(99)).thenReturn(false);

        // Act
        boolean isDeactivated = artistJPARepoImpl.deactivateArtistById(99);

        // Assert
        assertFalse(isDeactivated);
        verify(artistJPARepo, times(0)).deleteById(99);
    }
}
