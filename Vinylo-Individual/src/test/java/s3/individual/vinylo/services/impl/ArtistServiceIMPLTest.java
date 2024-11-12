package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.ArtistRepo;
import s3.individual.vinylo.serviceimpl.ArtistServiceIMPL;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceIMPLTest {

    @Mock
    private ArtistRepo artistRepoMock;

    @InjectMocks
    private ArtistServiceIMPL artistService;

    private Artist createArtist(int id, String name, String bio) {
        return Artist.builder()
                .id(id)
                .name(name)
                .bio(bio)
                .build();
    }

    @Test
    void testSaveArtist_ShouldSaveAndReturnNewArtist() {
        // Arrange
        Artist newArtist = createArtist(3, "Prince", "Purple rain");

        when(artistRepoMock.saveArtist(newArtist)).thenReturn(newArtist);

        // Act
        Artist savedArtist = artistService.saveArtist(null, newArtist);

        // Assert
        assertEquals(newArtist, savedArtist);
        verify(artistRepoMock).saveArtist(newArtist);
    }

    @Test
    void testSaveArtist_ShouldUpdateExistingArtist() {
        // Arrange
        int artistId = 2;
        Artist existingArtist = createArtist(artistId, "The Beatles", "ROCK&ROLL");
        Artist updatedArtist = createArtist(artistId, "The Beatles", "Updated bio");

        when(artistRepoMock.getArtistById(artistId)).thenReturn(existingArtist);
        when(artistRepoMock.saveArtist(existingArtist)).thenReturn(updatedArtist);

        // Act
        Artist result = artistService.saveArtist(artistId, updatedArtist);

        // Assert
        assertEquals("Updated bio", result.getBio());
        verify(artistRepoMock).saveArtist(existingArtist);
        verify(artistRepoMock).getArtistById(artistId);
    }

    @Test
    void testSaveArtist_ShouldThrowExceptionWhenArtistNotFoundForUpdate() {
        // Arrange
        int artistId = 2;
        Artist updatedArtist = createArtist(artistId, "The Beatles", "Updated bio");

        when(artistRepoMock.getArtistById(artistId)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class, () -> {
            artistService.saveArtist(artistId, updatedArtist);
        });
    }

    @Test
    void testGetArtists_ShouldReturnAllArtists() {
        // Arrange
        Artist artist1 = createArtist(1, "PLAYBOI CARTI", "syrup");
        Artist artist2 = createArtist(2, "The Beatles", "ROCK&ROLL");

        when(artistRepoMock.getArtists()).thenReturn(List.of(artist1, artist2));

        // Act
        List<Artist> actualArtists = artistService.getArtists();

        // Assert
        assertEquals(List.of(artist1, artist2), actualArtists);
        verify(artistRepoMock).getArtists();
    }

    @Test
    void testGeArtistById_ShouldReturnArtistWithGivenId() {
        // Arrange
        int artistId = 1;
        Artist expectedArtist = createArtist(artistId, "PLAYBOI CARTI", "syrup");

        when(artistRepoMock.getArtistById(artistId)).thenReturn(expectedArtist);

        // Act
        Artist actualArtist = artistService.getArtistById(artistId);

        // Assert
        assertEquals(expectedArtist, actualArtist);
        verify(artistRepoMock).getArtistById(artistId);
    }

    @Test
    void testGetArtistByName_ShouldReturnArtistWithGivenName() {
        // Arrange
        String artistName = "Prince";
        Artist expectedArtist = createArtist(3, artistName, "The Purple One");

        when(artistRepoMock.getArtistByName(artistName)).thenReturn(expectedArtist);

        // Act
        Artist actualArtist = artistService.getArtistByName(artistName);

        // Assert
        assertEquals(expectedArtist, actualArtist);
        verify(artistRepoMock).getArtistByName(artistName);
    }

    @Test
    void testDeactivateArtistById_ShouldReturnTrueWhenArtistIsDeactivated() {
        // Arrange
        int artistId = 1;
        when(artistRepoMock.deactivateArtistById(artistId)).thenReturn(true);

        // Act
        boolean isDeactivated = artistService.deactivateArtistById(artistId);

        // Assert
        assertTrue(isDeactivated);
        verify(artistRepoMock).deactivateArtistById(artistId);
    }

}
