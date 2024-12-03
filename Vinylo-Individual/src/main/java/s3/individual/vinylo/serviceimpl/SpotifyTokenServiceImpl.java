package s3.individual.vinylo.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import s3.individual.vinylo.domain.TokenResponse;
import s3.individual.vinylo.services.SpotifyTokenService;

@Service
public class SpotifyTokenServiceImpl implements SpotifyTokenService {

    @Value("${spotify.api.token_url}")
    private String tokenUrl;

    @Value("${spotify.api.client_id}")
    private String clientId;

    @Value("${spotify.api.client_secret}")
    private String clientSecret;

    private String accessToken;
    private long tokenExpirationTime;

    private static final int TOKEN_EXPIRATION_BUFFER = 60; // Buffer to refresh a little earlier (in seconds)

    // This method will fetch the access token from Spotify
    private String fetchAccessToken() {
        String url = tokenUrl;

        // Prepare the headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // Prepare the body for the request (client_credentials grant_type)
        String body = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;

        RestTemplate restTemplate = new RestTemplate();

        // Send the POST request to get the token
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(body, headers), TokenResponse.class);

        if (response.getBody() != null) {
            TokenResponse tokenResponse = response.getBody();
            if (tokenResponse != null && tokenResponse.getAccessToken() != null) {
                this.accessToken = tokenResponse.getAccessToken();
                this.tokenExpirationTime = System.currentTimeMillis()
                        + (tokenResponse.getExpiresIn() - TOKEN_EXPIRATION_BUFFER) * 1000;
                return accessToken;
            }
        }
        return null;
    }

    @Override
    public String getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() > tokenExpirationTime) {
            // Fetch new token if it doesn't exist or has expired
            return fetchAccessToken();
        }
        return accessToken;
    }

    // This method will be scheduled to refresh the token every hour
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void refreshAccessToken() {
        fetchAccessToken();
        System.out.println("Spotify token refreshed.");
    }
}
