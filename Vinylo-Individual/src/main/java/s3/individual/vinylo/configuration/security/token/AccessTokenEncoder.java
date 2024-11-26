package s3.individual.vinylo.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
