package s3.individual.vinylo.configuration.security.token;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Integer getId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}
