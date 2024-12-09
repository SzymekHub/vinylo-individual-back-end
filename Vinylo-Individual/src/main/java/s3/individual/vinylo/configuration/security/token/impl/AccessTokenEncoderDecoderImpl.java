package s3.individual.vinylo.configuration.security.token.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import s3.individual.vinylo.configuration.security.token.AccessToken;
import s3.individual.vinylo.configuration.security.token.AccessTokenDecoder;
import s3.individual.vinylo.configuration.security.token.AccessTokenEncoder;
import s3.individual.vinylo.configuration.security.token.exception.InvalidAccessTokenException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This class implements both `AccessTokenEncoder` and `AccessTokenDecoder`.
@Service
public class AccessTokenEncoderDecoderImpl implements AccessTokenEncoder, AccessTokenDecoder {
    private final Key key;

    // It uses a secret key that is injected from application properties for signing
    // and verifying JWTs.
    public AccessTokenEncoderDecoderImpl(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String encode(AccessToken accessToken) {
        Map<String, Object> claimsMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(accessToken.getRoles())) {
            claimsMap.put("roles", accessToken.getRoles());
        }
        if (accessToken.getId() != null) {
            claimsMap.put("id", accessToken.getId());
        }

        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(accessToken.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))
                .addClaims(claimsMap)
                .signWith(key)
                .compact();
    }

    @Override
    // this method parses a JWT and creates an `AccessTokenImpl` from its claims.
    public AccessToken decode(String accessTokenEncoded) {
        try {
            Jwt<?, Claims> jwt = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(accessTokenEncoded);
            Claims claims = jwt.getBody();

            List<String> roles = claims.get("roles", List.class);
            Integer id = claims.get("id", Integer.class);

            return new AccessTokenImpl(claims.getSubject(), id, roles);
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(e.getMessage());
        }
    }
}
