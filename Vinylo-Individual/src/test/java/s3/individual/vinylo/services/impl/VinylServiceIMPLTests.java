package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.serviceIMPL.VinylServiceIMPL;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VinylServiceIMPLTests {

    @Mock
    private VinylRepo vinylRepoMock;

    @InjectMocks
    private VinylServiceIMPL vinylService;

    @Test
    void testGetVinylById_ShouldReturnVinylWithGivenId() {
        // Arrange
        int vinylId = 1;
        Vinyl expectedVinyl = Vinyl.builder()
                .id(vinylId)
                .vinylType("EP")
                .title("ALL RED")
                .description("I AM MUSIC")
                .isReleased(true)
                .artist(Artist.builder()
                        .id(1)
                        .name("PLAYBOI CARTI")
                        .bio("syrup")
                        .build())
                .build();

        when(vinylRepoMock.getVinylById(vinylId)).thenReturn(expectedVinyl);

        // Act
        Vinyl actualVinyl = vinylService.getVinylById(vinylId);

        // Assert
        assertEquals(expectedVinyl, actualVinyl);
        verify(vinylRepoMock, times(1)).getVinylById(vinylId);
    }

    @Test
    void testSaveVinyl_ShouldSaveAndReturnNewVinyl() {
        // Arrange
        Vinyl newVinyl = Vinyl.builder()
                .vinylType("LP")
                .title("Rubber Soul")
                .description("ROCK&ROLL")
                .isReleased(true)
                .artist(Artist.builder()
                        .id(2)
                        .name("The Beatles")
                        .bio("Yeah yeah yeah")
                        .build())
                .build();

        when(vinylRepoMock.saveVinyl(newVinyl)).thenReturn(newVinyl);

        // Act
        Vinyl savedVinyl = vinylService.saveVinyl(newVinyl);

        // Assert
        assertEquals(newVinyl, savedVinyl);
        verify(vinylRepoMock).saveVinyl(newVinyl);
    }

    @Test
    void testSaveVinyl_ShouldUpdateExistingVinyl() {
        // Arrange
        int vinylId = 1;

        // Set up an existing vinyl in the mock repo
        Vinyl existingVinyl = Vinyl.builder()
                .id(vinylId)
                .vinylType("LP")
                .title("Original Album")
                .description("Original Description")
                .isReleased(false)
                .artist(new Artist(1, "Original Artist", "Bio"))
                .build();

        // Updated details for the vinyl
        Vinyl updatedVinyl = Vinyl.builder()
                .id(vinylId)
                .vinylType("EP")
                .title("Updated Album")
                .description("Updated Description")
                .isReleased(true)
                .artist(new Artist(1, "Updated Artist", "Updated Bio"))
                .build();

        // Mock the repository behavior
        when(vinylRepoMock.getVinylById(vinylId)).thenReturn(existingVinyl);
        when(vinylRepoMock.saveVinyl(existingVinyl)).thenReturn(updatedVinyl);

        // Act
        Vinyl result = vinylService.saveVinyl(vinylId, updatedVinyl);

        // Assert
        assertEquals("Updated Album", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("EP", result.getvinylType());
        assertEquals(true, result.getisReleased());
        assertEquals("Updated Artist", result.getArtist().getName());

        // Verify that the repository's save method was called with the updated vinyl
        verify(vinylRepoMock, times(1)).saveVinyl(existingVinyl);
        verify(vinylRepoMock, times(1)).getVinylById(vinylId);
    }

    @Test
    void testDeleteVinylById_ShouldReturnTrueWhenVinylIsDeleted() {
        // Arrange
        int vinylId = 1;
        when(vinylRepoMock.deleteVinylById(vinylId)).thenReturn(true);

        // Act
        boolean isDeleted = vinylService.deleteVinylById(vinylId);

        // Assert
        assertEquals(true, isDeleted);
        verify(vinylRepoMock, times(1)).deleteVinylById(vinylId);
    }
}
