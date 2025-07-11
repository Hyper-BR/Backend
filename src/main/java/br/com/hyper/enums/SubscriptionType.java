package br.com.hyper.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {
    FREE_LISTENER("Free Listener"),
    FREE_ARTIST("Free Artist"),
    SOLO("Solo"),
    STUDENT("Student"),
    FAMILY("Family"),
    ARTIST("Artist"),
    LABEL("Label");

    private final String description;

    SubscriptionType(String description) {
        this.description = description;
    }

}

