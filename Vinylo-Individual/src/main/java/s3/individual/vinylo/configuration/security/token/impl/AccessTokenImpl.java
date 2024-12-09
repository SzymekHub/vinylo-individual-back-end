package s3.individual.vinylo.configuration.security.token.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import s3.individual.vinylo.configuration.security.token.AccessToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String username;
    private final Integer id;
    private final Set<String> roles;

    public AccessTokenImpl(String username, Integer id, Collection<String> roles) {
        this.username = username;
        this.id = id;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}
