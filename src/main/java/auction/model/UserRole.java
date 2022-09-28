package auction.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.Sets;

import static auction.model.UserPermission.*;

public enum UserRole {
    BUYER(Sets.newHashSet(ADVERT_READ, ADD_PRICE)),
    SELLER(Sets.newHashSet(ADVERT_READ, ADVERT_ADD, ADVERT_EDIT, ADVERT_DELETE)),
    ADMIN(Sets.newHashSet(ADVERT_READ, ADVERT_ADD, ADVERT_EDIT, ADVERT_DELETE, ADMIN_PERMISSION));
    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
