package br.com.hyper.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    LABEL("LABEL"),
    ARTIST("ARTIST"),
    CUSTOMER("CUSTOMER");

    private final String role;

    UserRole(String role){
        this.role = role;
    }
}
