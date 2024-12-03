package s3.individual.vinylo.domain;

import java.util.Map;

public class SpotifyAlbumResponse {
    private Map<String, String> externalUrls;

    public Map<String, String> getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(Map<String, String> externalUrls) {
        this.externalUrls = externalUrls;
    }
}