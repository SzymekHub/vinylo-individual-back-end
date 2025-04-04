package s3.individual.vinylo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import s3.individual.vinylo.domain.SpotifyAlbumResponse;
import s3.individual.vinylo.services.SpotifyService;
import s3.individual.vinylo.services.SpotifyTokenService;

@Service
public class SpotifyServiceIMPL implements SpotifyService {

    @Autowired
    private SpotifyTokenService spotifyTokenService;

    @Override
    public String getAlbumEmbedUrl(String albumId) {
        // Fetch the access token from SpotifyTokenService
        String accessToken = spotifyTokenService.getAccessToken();

        if (accessToken == null) {
            return null;
        }

        String url = "https://api.spotify.com/v1/albums/" + albumId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Spotify API call to fetch album details
        SpotifyAlbumResponse response = restTemplate.getForObject(url, SpotifyAlbumResponse.class);

        if (response != null) {
            return response.getExternalUrls().get("spotify");
        }
        return null;
    }

}
