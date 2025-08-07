package br.com.hyper.enums;

import lombok.Getter;

@Getter
public enum DistribuitionStatus {

    WAITING("WAITING", "Pending status"),
    IN_PROGRESS("IN_PROGRESS", "Canceled status"),
    DELIVERED("DELIVERED", "Refunded status"),
    FAILED("FAILED", "Paid status"),
    CANCELLED("CANCELLED", "Completed status");

    private final String code;
    private final String message;

    DistribuitionStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

