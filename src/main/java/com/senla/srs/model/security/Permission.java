package com.senla.srs.model.security;

public enum Permission {
    USERS_READ("users:read"),
    USERS_WRITE("users:write");
    //ToDo Remake to fit the model
//    DEVELOPERS_READ("developers:read"),
//    DEVELOPERS_WRITE("developers:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
