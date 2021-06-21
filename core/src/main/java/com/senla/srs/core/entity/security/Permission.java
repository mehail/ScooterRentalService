package com.senla.srs.core.entity.security;

public enum Permission {

    POINT_OF_RENTALS_READ("pointOfRentals:read"),
    POINT_OF_RENTALS_WRITE("pointOfRentals:write"),

    PROMO_CODS_READ("promoCods:read"),
    PROMO_CODS_READ_ALL("promoCods:readAll"),
    PROMO_CODS_WRITE("promoCods:write"),

    SCOOTERS_READ("scooters:read"),
    SCOOTERS_WRITE("scooters:write"),

    RENTAL_SESSIONS_READ("rentalSessions:read"),
    RENTAL_SESSIONS_WRITE("rentalSessions:write"),

    SCOOTER_TYPES_READ("scooterTypes:read"),
    SCOOTER_TYPES_WRITE("scooterTypes:write"),

    SEASON_TICKETS_READ("seasonTickets:read"),
    SEASON_TICKETS_WRITE("seasonTickets:write"),

    USERS_READ("users:read"),
    USERS_WRITE("users:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
