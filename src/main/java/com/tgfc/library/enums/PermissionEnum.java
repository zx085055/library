package com.tgfc.library.enums;

public enum PermissionEnum {
    ROLE_USER(Role.ADMIN),
    ROLE_ADMIN(Role.USER);

    public class Role{
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

    private String name;

    PermissionEnum(String name) {
        this.name=name;
    }
}
