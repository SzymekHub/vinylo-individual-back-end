package s3.individual.vinylo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import s3.individual.vinylo.Models.services.VinylService;
import s3.individual.vinylo.serviceIMPL.VinylServiceIMPL;
import s3.individual.vinylo.serviceIMPL.domain.Artist;
import s3.individual.vinylo.serviceIMPL.domain.Vinyl;
import s3.individual.vinylo.services.serviceMocks.MockVinylRepo;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class VinylServiceTest {

    private MockVinylRepo mockVinylRepo;
    private VinylService vinylService;

    @BeforeEach
    void setUp() {
        mockVinylRepo = new MockVinylRepo();
        vinylService = new VinylServiceIMPL(mockVinylRepo);
    }

    @Test
    void createNewVinylShouldReturnANewVinylRecord() {
        // Arrange

        Artist arist1 = new Artist(1, "Artist1", "Artist1 bio");

        Vinyl vinyl = new Vinyl(10, "LP", "Test Album", "Test Description", false, arist1);

        // Act - Execute the method to be tested
        Vinyl result = vinylService.createNewVinyl(vinyl);

        // Assert - Check if the method post-condition is as expected
        assertEquals(vinyl, result);
        assertTrue(mockVinylRepo.getVinyls().contains(vinyl));

         /*  or i can do it like this as well
        assertNotNull(result);
        assertEquals("Test Album", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertFalse(result.getisReleased());
        */
    }

    @Test
    void getVinylByIdShouldReturnAVinylRecordWithTheCorrectId() {
        // Arrange
        Artist artist2 = new Artist(2, "Artist2", "Artist2 bio");

        Vinyl vinyl = new Vinyl(10, "LP", "Test2 Album", "Test2 Description", false, artist2);
        mockVinylRepo.createNewVinyl(vinyl);

        // Act - Execute the method to be tested
        Vinyl result = vinylService.getVinylById(10);

        // Assert - Check if the method post-condition is as expected
        assertEquals(vinyl, result);
    }

    @Test
    void getVinylsShouldReturnAllVinylRecords() {
        // Arrange
        Artist artist2 = new Artist(2, "Artist2", "Artist2 bio");
        Artist artist3 = new Artist(3, "Artist3", "Artist3 bio");

        
        Vinyl vinyl = new Vinyl(10, "LP", "Test2 Album", "Test2 Description", false, artist2);
        Vinyl vinyl2 = new Vinyl(12, "EP", "Test3 Album", "Test3 Description", true, artist3);
        mockVinylRepo.createNewVinyl(vinyl);
        mockVinylRepo.createNewVinyl(vinyl2);

        // Act - Execute the method to be tested
        List<Vinyl> vinyls = vinylService.getVinyls();

        // Assert - Check if the method post-condition is as expected
        assertEquals(2, vinyls.size() );
        assertTrue(vinyls.contains(vinyl));
        assertTrue(vinyls.contains(vinyl2));
    }

    @Test
    void replaceVinylShouldReplaceAnExistingVinylByIdWithNewData() {
        // Arrange
        Artist artist2 = new Artist(2, "Artist2", "Artist2 bio");
        Artist artist3 = new Artist(3, "Artist3", "Artist3 bio");

        
        Vinyl existingVinyl = new Vinyl(10, "LP", "Test2 Album", "Test2 Description", false, artist2);
        Vinyl newVinyl = new Vinyl(10, "EP", "Test3 Album", "Test3 Description", true, artist3);
        mockVinylRepo.createNewVinyl(existingVinyl);

        // Act - Execute the method to be tested
        Vinyl vinyl = vinylService.replaceVinyl(10, newVinyl);

        // Assert - Check if the method post-condition is as expected
        assertEquals(newVinyl.getArtist(), vinyl.getArtist());
        assertEquals(newVinyl.getisReleased(), vinyl.getisReleased());
        assertEquals(newVinyl.getDescription(), vinyl.getDescription());
        assertEquals(newVinyl.getTitle(), vinyl.getTitle());
        assertEquals(newVinyl.getvinylType(), vinyl.getvinylType());
        assertEquals(existingVinyl, vinyl);
    }

    @Test
    void deleteVinylByIdShouldDeleteAnExistingVinylById() {
        // Arrange
        Artist artist2 = new Artist(2, "Artist2", "Artist2 bio");
        Vinyl vinyl = new Vinyl(10, "LP", "Test Album", "Test2 Description", false, artist2);
        mockVinylRepo.createNewVinyl(vinyl);

        // Act - Execute the method to be tested
        boolean result = vinylService.deleteVinylById(10);

        // Assert - Check if the method post-condition is as expected
        assertTrue(result);
        assertFalse(mockVinylRepo.getVinyls().contains(vinyl));
    }
}