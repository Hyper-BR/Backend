package br.com.hyper.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {
    FREE("Free"),
    PREMIUM("Premium"),
    ARTIST("Artist"),
    LABEL("Label");

    private final String description;

    SubscriptionType(String description) {
        this.description = description;
    }

}

