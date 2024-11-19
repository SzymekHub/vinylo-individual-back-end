package s3.individual.vinylo.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.services.ArtistService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ArtistControllerTest {

    @Autowired
    // mockMvc is injected by Spring and will provide us means to send http requests
    // to our web application and also check the responses.
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    @Test
    void testCreateArtist_ShouldCreateAndReturn201_WhenRequestValid() throws Exception {

        // Arrange
        Artist createdArtist = Artist.builder()
                .id(666)
                .name("The Test Artist")
                .bio("Yeah yeah I can sing")
                .build();

        when(artistService.saveArtist(eq(null),
                any(Artist.class))).thenReturn(createdArtist);

        // Act
        mockMvc.perform(post("/artist")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                        "name": "The Test Artist",
                        "bio": "Yeah yeah I can sing"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Artist created successfully"));

        // Assert
        verify(artistService).saveArtist(eq(null), any(Artist.class));

    }

    @Test
    void testCreateArtist_ShouldCreateAndReturn400_WhenMIssingFields() throws Exception {

        // Assert
        mockMvc.perform(post("/artist")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                        "name": "",
                        "bio": "Yeah yeah I can sing"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isBadRequest());
        // Assert
        verifyNoInteractions(artistService);

    }

    @Test
    void testGetArtistById_shouldReturn200RespondWithArtistByID() throws Exception {

        // Arrange
        int artistId = 69;
        Artist artist = Artist.builder()
                .id(artistId)
                .name("The Test Artist1")
                .bio("Yeah yeah I can sing")
                .build();

        when(artistService.getArtistById(artistId)).thenReturn(artist);

        // Act and Assert
        mockMvc.perform(get("/artist/{id}", artistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(artistId))
                .andExpect(jsonPath("$.name").value("The Test Artist1"))
                .andExpect(jsonPath("$.bio").value("Yeah yeah I can sing"));
        verify(artistService).getArtistById(artistId);

    }

    @Test
    void testGetArtistById_shouldReturn404_WhenArtistNotFound() throws Exception {

        // Arrange
        int artistId = 999;
        when(artistService.getArtistById(artistId)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/artist/{id}", artistId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Artist not found"));

        verify(artistService).getArtistById(artistId);

    }

    @Test
    void testGetArtists_ShouldReturn200RespondWithArtistArray() throws Exception {

        // Arrange
        List<Artist> artists = List.of(
                Artist.builder()
                        .id(1)
                        .name("The Test Artist1")
                        .bio("Yeah yeah I can sing")
                        .build(),
                Artist.builder()
                        .id(2)
                        .name("The Test Artist2")
                        .bio("Yeah yeah I cannot sing")
                        .build());

        when(artistService.getArtists()).thenReturn(artists);

        // Act and Assert
        mockMvc.perform(get("/artist"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.artists[0].id").value(1))
                .andExpect(jsonPath("$.artists[0].name").value("The Test Artist1"))
                .andExpect(jsonPath("$.artists[1].id").value(2))
                .andExpect(jsonPath("$.artists[1].name").value("The Test Artist2"));

        verify(artistService).getArtists();
    }

    @Test
    void testReplaceArtistBio_shouldReturn200_whenSuccessful() throws Exception {

        // Arrange
        int artistId = 1;
        Artist existingArtistBio = Artist.builder()
                .id(artistId)
                .name("The Test Artist1")
                .bio("OG Yeah yeah I can sing")
                .build();

        Artist updatedArtistBio = Artist.builder()
                .id(artistId)
                .name(existingArtistBio.getName())
                .bio("NEW Yeah yeah I can sing")
                .build();
        when(artistService.saveArtist(eq(artistId), any(Artist.class))).thenReturn(updatedArtistBio);
    }
}
