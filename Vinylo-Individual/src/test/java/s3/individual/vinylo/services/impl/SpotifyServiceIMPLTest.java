package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import s3.individual.vinylo.domain.SpotifyAlbumResponse;
import s3.individual.vinylo.serviceimpl.SpotifyServiceIMPL;
import s3.individual.vinylo.services.SpotifyTokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class SpotifyServiceIMPLTest {

    @Mock
    private SpotifyTokenService spotifyTokenServiceMock;

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private SpotifyServiceIMPL spotifyService;

    private String testAlbumId;
    private SpotifyAlbumResponse testSpotifyAlbumResponse;

    @BeforeEach
    void setUp() {
        testAlbumId = "testAlbumId";

        testSpotifyAlbumResponse = new SpotifyAlbumResponse();
        testSpotifyAlbumResponse.setExternalUrls(Map.of("spotify", "https://open.spotify.com/album/testAlbumId"));
    }

    @Test
    void testGetAlbumEmbedUrl_ShouldReturnNullWhenAccessTokenIsNull() {
        // Arrange
        when(spotifyTokenServiceMock.getAccessToken()).thenReturn(null);

        // Act
        String result = spotifyService.getAlbumEmbedUrl(testAlbumId);

        // Assert
        assertNull(result);
        verify(spotifyTokenServiceMock).getAccessToken();
        verifyNoInteractions(restTemplateMock);
    }
}
