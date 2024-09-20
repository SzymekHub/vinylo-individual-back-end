package s3.individual.vinylo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import s3.individual.vinylo.business.Vinyl;
import s3.individual.vinylo.services.serviceMocks.MockVinylRepo;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class VinylServiceTest {

    private MockVinylRepo mockVinylRepo;
    private VinylService vinylService;

    @BeforeEach
    void setUp() {
        mockVinylRepo = new MockVinylRepo();
        vinylService = new VinylService(mockVinylRepo);
    }

    @Test
    void createNewVinylShouldReturnANewVinylRecord() {
        // Arrange
        Vinyl vinyl = new Vinyl(10, "LP", "Test Album", "Test Description", false, "Artist");

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
    void getVinylById() {
        // Arrange
        Vinyl vinyl = new Vinyl(10, "LP", "Test2 Album", "Test2 Description", false, "Artist2");
        mockVinylRepo.createNewVinyl(vinyl);

        // Act - Execute the method to be tested
        Vinyl result = vinylService.getVinylById(10);

        // Assert - Check if the method post-condition is as expected
        assertEquals(vinyl, result);
    }

    @Test
    void getVinyls() {
        // Arrange
        Vinyl vinyl = new Vinyl(10, "LP", "Test2 Album", "Test2 Description", false, "Artist2");
        Vinyl vinyl2 = new Vinyl(12, "EP", "Test3 Album", "Test3 Description", true, "Artist3");
        mockVinylRepo.createNewVinyl(vinyl);
        mockVinylRepo.createNewVinyl(vinyl2);

        // Act - Execute the method to be tested
        ArrayList<Vinyl> vinyls = vinylService.getVinyls();

        // Assert - Check if the method post-condition is as expected
        assertEquals(2, vinyls.size() );
        assertTrue(vinyls.contains(vinyl));
        assertTrue(vinyls.contains(vinyl2));
    }

    @Test
    void replaceVinyl() {
        // Arrange
        Vinyl existingVinyl = new Vinyl(10, "LP", "Test2 Album", "Test2 Description", false, "Artist2");
        Vinyl newVinyl = new Vinyl(10, "EP", "Test3 Album", "Test3 Description", true, "Artist3");
        mockVinylRepo.createNewVinyl(existingVinyl);

        // Act - Execute the method to be tested
        Vinyl vinyl = vinylService.replaceVinyl(10, newVinyl);

        // Assert - Check if the method post-condition is as expected
        assertEquals(newVinyl.getAristName(), vinyl.getAristName());
        assertEquals(newVinyl.getisReleased(), vinyl.getisReleased());
        assertEquals(newVinyl.getDescription(), vinyl.getDescription());
        assertEquals(newVinyl.getName(), vinyl.getName());
        assertEquals(newVinyl.getvinylType(), vinyl.getvinylType());
        assertEquals(existingVinyl, vinyl);
    }

    @Test
    void deleteVinylById() {
        // Arrange
        Vinyl vinyl = new Vinyl(10, "LP", "Test Album", "Test2 Description", false, "Artist2");
        mockVinylRepo.createNewVinyl(vinyl);

        // Act - Execute the method to be tested
        boolean result = vinylService.deleteVinylById(10);

        // Assert - Check if the method post-condition is as expected
        assertTrue(result);
        assertFalse(mockVinylRepo.getVinyls().contains(vinyl));
    }
}