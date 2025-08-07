package br.com.hyper.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("PENDING", "Pending status"),
    CANCELLED("CANCELLED", "Canceled status"),
    REFUNDED("REFUNDED", "Refunded status"),
    PAID("PAID", "Paid status"),
    COMPLETED("COMPLETED", "Completed status");

    private final String code;
    private final String message;

    OrderStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

