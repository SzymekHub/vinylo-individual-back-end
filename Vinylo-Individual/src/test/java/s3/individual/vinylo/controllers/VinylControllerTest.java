package s3.individual.vinylo.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.services.VinylService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//This is partial integration test.
// After implementing security in my project, you will probably need to switch from annotation
// @WebMvcTest(<Controller Name>.class)

// to

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// That is needed to make sure all your security configuration is properly
// loaded
class VinylControllerTest {

    @Autowired
    // mockMvc is injected by Spring and will provide us means to send http requests
    // to our web application and also check the responses.
    private MockMvc mockMvc;

    @MockBean
    private VinylService vinylService;
    // Annotating them with @MockBean tells Spring these fields should be Mockito
    // mocks. Spring automatically takes care of injecting the mocks into
    // VinylContoller.

    @MockBean
    private ArtistService artistService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddVinyl_shouldCreateAndReturn201_WhenRequestValid() throws Exception {

        // Arrange
        Vinyl createdVinyl = Vinyl.builder()
                .id(100)
                .vinylType(VinylTypeEnum.LP_12_INCH)
                .speed(SpeedEnum.RPM_33_1_3)
                .title("Abbey Road")
                .description("A legendary Beatles album")
                .state(StateEnum.REMASTERED)
                .color(VinylColorEnum.WHITE)
                .isReleased(true)
                .artist(new Artist(1, "The Beatles", "Famous British rock band"))
                .build();

        // Use eq(null) to match the null id value as per the addVinyl logic
        when(vinylService.saveVinyl(eq(null),
                any(Vinyl.class))).thenReturn(createdVinyl);
        when(artistService.getArtistById(1))
                .thenReturn(new Artist(1, "The Beatles", "Famous British rock band"));

        // Act
        mockMvc.perform(post("/vinyls")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                                {
                                        "vinylType": "LP_12_INCH",
                                        "speed": "RPM_33_1_3",
                                        "title": "Abbey Road",
                                        "description": "A legendary Beatles album",
                                        "state": "REMASTERED",
                                        "color": "WHITE",
                                        "isReleased": true,
                                        "artist_id": 1
                                }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Vinyl created successfully"));

        // Assert
        verify(vinylService).saveVinyl(eq(null), any(Vinyl.class));
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

        verifyNoInteractions(vinylService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddVinyl_shouldReturn404_WhenArtistNotFound() throws Exception {
        // Arrange
        int nonExistentArtistId = 999;
        when(artistService.getArtistById(nonExistentArtistId)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(post("/vinyls")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                                "vinylType": "LP_12_INCH",
                                "speed": "RPM_33_1_3",
                                "title": "Abbey Road",
                                "description": "A legendary Beatles album",
                                "state": "REMASTERED",
                                "color": "WHITE",
                                "isReleased": true,
                                "artist_id": 999
                        }
                        """))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Artist with ID " + nonExistentArtistId + " not found."));

        verify(artistService).getArtistById(nonExistentArtistId);
        verifyNoInteractions(vinylService);
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
    void testGetVinyls_shouldReturn200RespondWithVinylsArray() throws Exception {

        // Arrange
        List<Vinyl> vinyls = List.of(

                Vinyl.builder().id(1).vinylType(VinylTypeEnum.LP_12_INCH)
                        .speed(SpeedEnum.RPM_33_1_3)
                        .title("Dark Side of the Moon")
                        .description("A classic album")
                        .state(StateEnum.REMASTERED)
                        .color(VinylColorEnum.WHITE)
                        .isReleased(true).artist(new Artist(1, "Pink Floyd", "I am so Pink"))
                        .build(),
                Vinyl.builder().id(2).vinylType(VinylTypeEnum.SINGLE_7_INCH)
                        .speed(SpeedEnum.RPM_33_1_3)
                        .title("Imagine")
                        .description("A legendary song")
                        .state(StateEnum.REMASTERED)
                        .color(VinylColorEnum.WHITE)
                        .isReleased(true)
                        .artist(new Artist(2, "John Lennon", "I love my wife. lol jk"))
                        .build());

        when(vinylService.getVinyls(0, 5)).thenReturn(vinyls);

        // Act and Assert
        mockMvc.perform(get("/vinyls"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vinyls[0].id").value(1))
                .andExpect(jsonPath("$.vinyls[0].title").value("Dark Side of the Moon"))
                .andExpect(jsonPath("$.vinyls[1].id").value(2))
                .andExpect(jsonPath("$.vinyls[1].title").value("Imagine"));

        verify(vinylService).getVinyls(0, 5);
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
    void testGetVinylById_shouldReturn200RespondWithVinylByID() throws Exception {
        // Arrange
        int vinylId = 1;
        Vinyl vinyl = Vinyl.builder()
                .id(vinylId)
                .vinylType(VinylTypeEnum.LP_12_INCH)
                .speed(SpeedEnum.RPM_33_1_3)
                .title("Abbey Road")
                .description("A legendary Beatles album")
                .state(StateEnum.REMASTERED)
                .color(VinylColorEnum.WHITE)
                .isReleased(true)
                .artist(new Artist(1, "The Beatles", "Famous British rock band"))
                .build();

        when(vinylService.getVinylById(vinylId)).thenReturn(vinyl);

        // Act and Assert
        mockMvc.perform(get("/vinyls/{id}", vinylId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(vinylId))
                .andExpect(jsonPath("$.vinylType").value("LP_12_INCH"))
                .andExpect(jsonPath("$.title").value("Abbey Road"))
                .andExpect(jsonPath("$.description").value("A legendary Beatles album"))
                .andExpect(jsonPath("$.isReleased").value(true))
                .andExpect(jsonPath("$.artist_id").value(1));

        verify(vinylService).getVinylById(vinylId);
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "REGULAR", "PREMIUM" })
    void testGetVinylById_shouldReturn404_WhenVinylNotFound() throws Exception {
        // Arrange
        int vinylId = 999;

        when(vinylService.getVinylById(vinylId)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/vinyls/{id}", vinylId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vinyl record not found"));

        verify(vinylService).getVinylById(vinylId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testReplaceVinyl_shouldReturn200_WhenSuccessful() throws Exception {
        // Arrange
        int vinylId = 1;
        Vinyl existingVinyl = Vinyl.builder()
                .id(vinylId)
                .vinylType(VinylTypeEnum.LP_12_INCH)
                .speed(SpeedEnum.RPM_33_1_3)
                .title("Abbey Road")
                .description("A legendary Beatles album")
                .state(StateEnum.REMASTERED)
                .color(VinylColorEnum.WHITE)
                .isReleased(true)
                .artist(new Artist(1, "The Beatles", "Famous British rock band"))
                .build();

        Vinyl updatedVinyl = Vinyl.builder()
                .id(vinylId)
                .vinylType(existingVinyl.getVinylType())
                .speed(existingVinyl.getSpeed())
                .title(existingVinyl.getTitle())
                .description("A HYPER legendary Beatles album")
                .state(existingVinyl.getState())
                .color(existingVinyl.getColor())
                .isReleased(existingVinyl.getIsReleased())
                .artist(existingVinyl.getArtist())
                .build();

        when(vinylService.saveVinyl(eq(vinylId), any(Vinyl.class))).thenReturn(updatedVinyl);

        String jsonContent = """
                {
                        "vinylType": "LP_12_INCH",
                        "speed": "RPM_33_1_3",
                        "title": "Abbey Road",
                        "description": "A HYPER legendary Beatles album",
                        "state": "REMASTERED",
                        "color": "WHITE",
                        "isReleased": true,
                        "artist_id": 1
                }
                """;

        mockMvc.perform(put("/vinyls/{id}", vinylId)
                .contentType(APPLICATION_JSON_VALUE)
                .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("A HYPER legendary Beatles album"));

        verify(vinylService).saveVinyl(eq(vinylId), any(Vinyl.class));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteVinylById_shouldReturn201_WhenRequestValid() throws Exception {
        // Arrange
        int vinylId = 150;
        when(vinylService.deleteVinylById(vinylId)).thenReturn(true);

        // Act and Assert
        mockMvc.perform(delete("/vinyls/{id}", vinylId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Vinyl record with id " + vinylId + " was successfully deleted."));

        verify(vinylService).deleteVinylById(vinylId);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteVinylById_shouldReturn404_WhenVinylNotFound() throws Exception {
        // Arrange
        int vinylId = 179;
        when(vinylService.deleteVinylById(vinylId)).thenReturn(false);

        // Act and Assert
        mockMvc.perform(delete("/vinyls/{id}", vinylId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vinyl record with id " + vinylId + " was not found."));

        verify(vinylService).deleteVinylById(vinylId);

    }

}
