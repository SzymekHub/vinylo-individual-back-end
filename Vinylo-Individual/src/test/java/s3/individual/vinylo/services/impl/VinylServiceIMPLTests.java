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
// import s3.individual.vinylo.exceptions.CustomNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class VinylServiceIMPLTests {

        @Mock
        private VinylRepo vinylRepoMock;

        @InjectMocks
        private VinylServiceIMPL vinylService;

        private Artist createArtist(int id, String name, String bio) {
                return Artist.builder()
                                .id(id)
                                .name(name)
                                .bio(bio)
                                .build();
        }

        private Vinyl createVinyl(int id, String title, String vinylType, String description, boolean isReleased,
                        Artist artist) {
                return Vinyl.builder()
                                .id(id)
                                .title(title)
                                .vinylType(vinylType)
                                .description(description)
                                .isReleased(isReleased)
                                .artist(artist)
                                .build();
        }

        @Test
        void testSaveVinyl_ShouldSaveAndReturnNewVinyl() {
                // Arrange
                Vinyl newVinyl = createVinyl(2,
                                "Rubber Soul",
                                "LP",
                                "ROCK&ROLL",
                                true,
                                createArtist(2, "The Beatles", "Yeah yeah yeah"));

                when(vinylRepoMock.saveVinyl(newVinyl)).thenReturn(newVinyl);

                // Act
                Vinyl savedVinyl = vinylService.saveVinyl(null, newVinyl);

                // Assert
                assertEquals(newVinyl, savedVinyl);
                verify(vinylRepoMock).saveVinyl(newVinyl);
        }

        @Test
        void testSaveVinyl_ShouldUpdateExistingVinyl() {
                // Arrange
                int vinylId = 2;

                // Set up an existing vinyl in the mock repo
                Vinyl existingVinyl = createVinyl(vinylId,
                                "Rubber Soul",
                                "LP",
                                "ROCK&ROLL",
                                true,
                                createArtist(2, "The Beatles", "Yeah yeah yeah"));

                // Updated details for the vinyl
                Vinyl updatedVinyl = createVinyl(vinylId, "Soul Rubber", "LP", "ROLL&ROCK", false,
                                createArtist(2, "The Beatles", "yeah Yeah yeah"));

                // Mock the repository behavior
                when(vinylRepoMock.getVinylById(vinylId)).thenReturn(existingVinyl);
                when(vinylRepoMock.saveVinyl(existingVinyl)).thenReturn(updatedVinyl);

                // Act
                Vinyl result = vinylService.saveVinyl(vinylId, updatedVinyl);

                // Assert
                assertEquals("Soul Rubber", result.getTitle());
                assertEquals("ROLL&ROCK", result.getDescription());
                assertEquals("LP", result.getvinylType());
                assertEquals(false, result.getisReleased());
                assertEquals("The Beatles", result.getArtist().getName());

                // Verify that the repository's save method was called with the updated vinyl
                verify(vinylRepoMock).saveVinyl(existingVinyl);
                verify(vinylRepoMock).getVinylById(vinylId);
        }

        // @Test
        // void testSaveVinyl_ShouldThrowExceptionWhenVinylNotFoundForUpdate() {
        // // Arrange
        // int vinylId = 2;
        // Vinyl updatedVinyl = createVinyl(vinylId, "Soul Rubber", "LP", "ROLL&ROCK",
        // false,
        // createArtist(2, "The Beatles", "yeah yeah"));

        // when(vinylRepoMock.getVinylById(vinylId)).thenReturn(null);

        // // Act & Assert
        // assertThrows(CustomNotFoundException.class, () -> {
        // vinylService.saveVinyl(vinylId, updatedVinyl);
        // });
        // }

        @Test
        void testGetVinylById_ShouldReturnVinylWithGivenId() {
                // Arrange
                int vinylId = 1;
                Vinyl expectedVinyl = createVinyl(vinylId,
                                "ALL RED",
                                "EP",
                                "I AM MUSIC",
                                true,
                                createArtist(1, "PLAYBOI CARTI", "syrup"));

                when(vinylRepoMock.getVinylById(vinylId)).thenReturn(expectedVinyl);

                // Act
                Vinyl actualVinyl = vinylService.getVinylById(vinylId);

                // Assert
                assertEquals(expectedVinyl, actualVinyl);
                verify(vinylRepoMock).getVinylById(vinylId);
        }

        @Test
        void getVinyls_ShouldReturnAllVinylRecords() {
                // Arrange
                Vinyl vinyl1 = createVinyl(3,
                                "Abbey Road",
                                "LP",
                                "Classic Beatles album",
                                true,
                                createArtist(2, "The Beatles", "Legendary rock band"));

                Vinyl vinyl2 = createVinyl(4,
                                "Purple Rain",
                                "EP",
                                "Iconic Prince single",
                                true,
                                createArtist(3, "Prince", "The Purple One"));

                when(vinylRepoMock.getVinyls()).thenReturn(List.of(vinyl1, vinyl2));

                // Act
                List<Vinyl> actualVinyls = vinylService.getVinyls();

                // Assert
                assertEquals(List.of(vinyl1, vinyl2), actualVinyls);
                verify(vinylRepoMock).getVinyls();
        }

        @Test
        void testDeleteVinylById_ShouldReturnTrueWhenVinylIsDeleted() {
                // Arrange
                int vinylId = 1;
                // Mock the repository to return true when delete is called
                when(vinylRepoMock.deleteVinylById(vinylId)).thenReturn(true);

                // Act
                boolean isDeleted = vinylService.deleteVinylById(vinylId);

                // Assert
                // Verify the return value
                assertEquals(true, isDeleted);
                // Ensure the method was called with the correct
                // parameter
                verify(vinylRepoMock).deleteVinylById(vinylId);
        }
}
