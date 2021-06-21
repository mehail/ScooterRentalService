package com.senla.srs.core.entity.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {

    USER(Set.of(Permission.POINT_OF_RENTALS_READ,
            Permission.PROMO_CODS_READ,
            Permission.RENTAL_SESSIONS_READ,
            Permission.SCOOTERS_READ,
            Permission.SCOOTER_TYPES_READ,
            Permission.SEASON_TICKETS_READ,
            Permission.SEASON_TICKETS_WRITE,
            Permission.USERS_READ)),

    ADMIN(Set.of(Permission.POINT_OF_RENTALS_READ,
            Permission.POINT_OF_RENTALS_WRITE,
            Permission.PROMO_CODS_READ,
            Permission.PROMO_CODS_READ_ALL,
            Permission.PROMO_CODS_WRITE,
            Permission.SCOOTERS_READ,
            Permission.SCOOTERS_WRITE,
            Permission.RENTAL_SESSIONS_READ,
            Permission.RENTAL_SESSIONS_WRITE,
            Permission.SCOOTER_TYPES_READ,
            Permission.SCOOTER_TYPES_WRITE,
            Permission.SEASON_TICKETS_READ,
            Permission.SEASON_TICKETS_WRITE,
            Permission.USERS_READ,
            Permission.USERS_WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

}
