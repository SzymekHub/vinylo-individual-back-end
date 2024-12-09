package s3.individual.vinylo.configuration.security.token;

import java.util.Set;

//this interface defines the structure of the access token
public interface AccessToken {
    String getUsername();

    Integer getId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}
