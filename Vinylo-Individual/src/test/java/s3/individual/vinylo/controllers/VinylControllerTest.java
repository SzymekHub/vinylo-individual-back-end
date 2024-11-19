package s3.individual.vinylo.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.services.ArtistService;
import s3.individual.vinylo.services.VinylService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//This is partial integration test.
// After implementing security in my project, you will probably need to switch from annotation
// @WebMvcTest(<Controller Name>.class)

// to

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
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
        void testCreateNewVinyl_shouldCreateAndReturn201_WhenRequestValid() throws Exception {

                // Arrange
                Vinyl createdVinyl = Vinyl.builder()
                                .id(100)
                                .vinylType("LP")
                                .title("Abbey Road")
                                .description("A legendary Beatles album")
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
                                                "title": "Abbey Road",
                                                "description": "A legendary Beatles album",
                                                "vinylType": "LP",
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
        void testCreateNewVinyl_shouldCreateAndReturn400_WhenMissingFields() throws Exception {
                // Assert
                mockMvc.perform(post("/vinyls")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content("""
                                                {
                                                "vinylType": "LP",
                                                "title": "Abbey Road",
                                                "description": "",
                                                "isReleased": true,
                                                "artist_id": 1
                                                }
                                                """))
                                .andDo(print())
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(vinylService);
        }

        @Test
        void testGetVinyls_shouldReturn200RespondWithVinylsArray() throws Exception {

                // Arrange
                List<Vinyl> vinyls = List.of(
                                Vinyl.builder().id(1).vinylType("LP").title("Dark Side of the Moon")
                                                .description("A classic album")
                                                .isReleased(true).artist(new Artist(1, "Pink Floyd", "I am so Pink"))
                                                .build(),
                                Vinyl.builder().id(2).vinylType("Single").title("Imagine")
                                                .description("A legendary song")
                                                .isReleased(true)
                                                .artist(new Artist(2, "John Lennon", "I love my wife. lol jk"))
                                                .build());

                when(vinylService.getVinyls()).thenReturn(vinyls);

                // Act and Assert
                mockMvc.perform(get("/vinyls"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.vinyls[0].id").value(1))
                                .andExpect(jsonPath("$.vinyls[0].title").value("Dark Side of the Moon"))
                                .andExpect(jsonPath("$.vinyls[1].id").value(2))
                                .andExpect(jsonPath("$.vinyls[1].title").value("Imagine"));

                verify(vinylService).getVinyls();
        }

}
