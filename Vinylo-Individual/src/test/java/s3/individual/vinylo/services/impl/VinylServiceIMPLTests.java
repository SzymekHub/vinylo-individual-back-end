package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import s3.individual.vinylo.serviceimpl.VinylServiceIMPL;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.exceptions.CustomNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private Vinyl createVinyl(int id, VinylTypeEnum vinylType, SpeedEnum speed, String title, String description,
            StateEnum state,
            VinylColorEnum color,
            boolean isReleased,
            Artist artist) {
        return Vinyl.builder()
                .id(id)
                .vinylType(vinylType)
                .speed(speed)
                .title(title)
                .description(description)
                .state(state)
                .color(color)
                .isReleased(isReleased)
                .artist(artist)
                .build();
    }

    @Test
    void testSaveVinyl_ShouldSaveAndReturnNewVinyl() {
        // Arrange
        Vinyl newVinyl = createVinyl(2,
                VinylTypeEnum.EP,
                SpeedEnum.RPM_45,
                "Rubber Soul",
                "ROCK&ROLL",
                StateEnum.NEW,
                VinylColorEnum.COLORED,
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
                VinylTypeEnum.EP,
                SpeedEnum.RPM_45,
                "Rubber Soul",
                "ROCK&ROLL",
                StateEnum.NEW,
                VinylColorEnum.COLORED,
                true,
                createArtist(2, "The Beatles", "Yeah yeah yeah"));

        // Updated details for the vinyl
        Vinyl updatedVinyl = createVinyl(vinylId, VinylTypeEnum.LP_12_INCH, SpeedEnum.RPM_78, "Soul Rubber",
                "ROLL&ROCK", StateEnum.BROKEN, VinylColorEnum.BLACK, false,
                createArtist(2, "The Beatles", "yeah Yeah yeah"));

        // Mock the repository behavior
        when(vinylRepoMock.getVinylById(vinylId)).thenReturn(existingVinyl);
        when(vinylRepoMock.saveVinyl(existingVinyl)).thenReturn(updatedVinyl);

        // Act
        Vinyl result = vinylService.saveVinyl(vinylId, updatedVinyl);

        // Assert
        assertEquals("Soul Rubber", result.getTitle());
        assertEquals("ROLL&ROCK", result.getDescription());
        assertEquals(VinylTypeEnum.LP_12_INCH, result.getVinylType());
        assertEquals(false, result.getIsReleased());
        assertEquals("The Beatles", result.getArtist().getName());

        // Verify that the repository's save method was called with the updated vinyl
        verify(vinylRepoMock).saveVinyl(existingVinyl);
        verify(vinylRepoMock).getVinylById(vinylId);
    }

    @Test
    void testSaveVinyl_ShouldThrowExceptionWhenVinylNotFoundForUpdate() {
        // Arrange
        int vinylId = 2;
        Vinyl updatedVinyl = createVinyl(vinylId, VinylTypeEnum.EP,
                SpeedEnum.RPM_45,
                "Rubber Soul",
                "ROCK&ROLL",
                StateEnum.NEW,
                VinylColorEnum.COLORED,
                true,
                createArtist(2, "The Beatles", "yeah yeah"));

        when(vinylRepoMock.getVinylById(vinylId)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class, () -> {
            vinylService.saveVinyl(vinylId, updatedVinyl);
        });
    }

    @Test
    void testGetVinylById_ShouldReturnVinylWithGivenId() {
        // Arrange
        int vinylId = 1;
        Vinyl expectedVinyl = createVinyl(vinylId,
                VinylTypeEnum.EP,
                SpeedEnum.RPM_45,
                "ALL RED",
                "I AM MUSIC",
                StateEnum.LIMITED_EDITION,
                VinylColorEnum.COLORED,
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
                VinylTypeEnum.LP_12_INCH,
                SpeedEnum.RPM_45,
                "Abbey Road",
                "Classic Beatles album",
                StateEnum.ORIGINAL,
                VinylColorEnum.BLACK,
                true,
                createArtist(2, "The Beatles", "Legendary rock band"));

        Vinyl vinyl2 = createVinyl(4,
                VinylTypeEnum.EP,
                SpeedEnum.RPM_45,
                "Purple Rain",
                "Iconic Prince Single",
                StateEnum.ORIGINAL,
                VinylColorEnum.BLACK,
                true,
                createArtist(3, "Prince", "The Purple One"));

        when(vinylRepoMock.getVinyls(0, 5)).thenReturn(List.of(vinyl1, vinyl2));

        // Act
        List<Vinyl> actualVinyls = vinylService.getVinyls(0, 5);

        // Assert
        assertEquals(List.of(vinyl1, vinyl2), actualVinyls);
        verify(vinylRepoMock).getVinyls(0, 5);
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
