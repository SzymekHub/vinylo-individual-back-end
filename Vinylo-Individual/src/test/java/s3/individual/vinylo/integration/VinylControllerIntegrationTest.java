package s3.individual.vinylo.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.persistence.ArtistRepo;
import s3.individual.vinylo.persistence.VinylRepo;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class VinylControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VinylRepo vinylRepo;

    @Autowired
    private ArtistRepo artistRepo;

    @BeforeEach
    void setUp() {

        List<Vinyl> existingVinyls = vinylRepo.getVinyls(0, 5);

        if (existingVinyls.isEmpty()) {
            // Insert test data only if no vinyls exist
            // Arrange: Seed the in-memory database with test data
            Artist artist = new Artist();
            artist.setId(1);
            artist.setName("Test Artist");
            artist.setBio("Test BIO");

            artistRepo.saveArtist(artist); // Save the artist entity first

            Artist artist2 = new Artist();
            artist2.setId(2);
            artist2.setName("Test Artist2");
            artist2.setBio("Test BIO2");

            artistRepo.saveArtist(artist2); // Save the artist entity first

            Vinyl vinyl = new Vinyl();
            vinyl.setId(1);
            vinyl.setVinylType(VinylTypeEnum.valueOf("LP_12_INCH"));
            vinyl.setSpeed(SpeedEnum.valueOf("RPM_45"));
            vinyl.setTitle("Test Vinyl");
            vinyl.setDescription("Test Description");
            vinyl.setState(StateEnum.valueOf("NEW"));
            vinyl.setColor(VinylColorEnum.valueOf("COLORED"));
            vinyl.setIsReleased(true);
            vinyl.setArtist(artist); // Associate the saved artist

            vinylRepo.saveVinyl(vinyl);

            Vinyl vinyl2 = new Vinyl();
            vinyl2.setId(2);
            vinyl2.setVinylType(VinylTypeEnum.valueOf("EP"));
            vinyl2.setSpeed(SpeedEnum.valueOf("RPM_33_1_3"));
            vinyl2.setTitle("Test Vinyl2");
            vinyl2.setDescription("Test Description2");
            vinyl2.setState(StateEnum.valueOf("REMASTERED"));
            vinyl2.setColor(VinylColorEnum.valueOf("BLACK"));
            vinyl2.setIsReleased(false);
            vinyl2.setArtist(artist2); // Associate the saved artist

            vinylRepo.saveVinyl(vinyl2);
        }

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddVinyl_shouldCreateAndReturn201_WhenRequestValid() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/vinyls")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                                "vinylType": "LP_12_INCH",
                                "speed": "RPM_45",
                                "title": "New Vinyl",
                                "description": "New Description",
                                "state": "NEW",
                                "color": "COLORED",
                                "isReleased": true,
                                "artist_id": 1
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Vinyl created successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddVinyl_shouldCreateAndReturn400_WhenMissingFields() throws Exception {
        // Assert
        mockMvc.perform(post("/vinyls")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                        "vinylType": "LP_12_INCH",
                        "speed": "RPM_33_1_3",
                        "title": "Abbey Road",
                        "description": "",
                        "state": "REMASTERED",
                        "color": "WHITE",
                        "isReleased": true,
                        "artist_id": 1
                        }
                        """))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddVinyl_shouldReturn404_WhenArtistNotFound() throws Exception {

        // Act and Assert
        mockMvc.perform(post("/vinyls")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                                "vinylType": "LP_12_INCH",
                                "speed": "RPM_33_1_3",
                                "title": "New Vinyl",
                                "description": "New Description",
                                "state": "REMASTERED",
                                "color": "WHITE",
                                "isReleased": true,
                                "artist_id": 999
                        }
                        """))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Artist with ID 999 not found."));
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
    void testGetVinyls_shouldReturn200RespondWithVinylsArray() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/vinyls"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vinyls[*].id").exists())
                .andExpect(jsonPath("$.vinyls[*].title").exists());

    }

    @Test
    @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
    void testGetVinylById_shouldReturn200RespondWithVinylByID() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/vinyls/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.vinylType").value("LP_12_INCH"))
                .andExpect(jsonPath("$.title").value("Test Vinyl"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.isReleased").value(true))
                .andExpect(jsonPath("$.artist_id").value(1));
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
    void testGetVinylById_shouldReturn404_WhenVinylNotFound() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/vinyls/{id}", 999))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vinyl record not found"));

    }

    // !! to be fixed
    // @Test
    // @WithMockUser(roles = "ADMIN")
    // void testReplaceVinyl_shouldReturn200_WhenSuccessful() throws Exception {

    // String jsonContent = """
    // {
    // "vinylType": "LP_12_INCH",
    // "speed": "RPM_45",
    // "title": "Test Vinyl",
    // "description": "Updated Description",
    // "state": "NEW",
    // "color": "COLORED",
    // "isReleased": true,
    // "artist_id": 1
    // }
    // """;

    // mockMvc.perform(put("/vinyls/{id}", 1)
    // .contentType(APPLICATION_JSON_VALUE)
    // .content(jsonContent))
    // .andDo(print())
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.description").value("Updated Description"));

    // }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteVinylById_shouldReturn201_WhenRequestValid() throws Exception {
        // Act and Assert
        mockMvc.perform(delete("/vinyls/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Vinyl record with id " + 2 + " was successfully deleted."));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteVinylById_shouldReturn404_WhenVinylNotFound() throws Exception {

        // Act and Assert
        mockMvc.perform(delete("/vinyls/{id}", 33))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vinyl record with id " + 33 + " was not found."));

    }

}
