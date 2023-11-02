package com.example.SiteProject.SiteProject.exceptions;

import com.example.SiteProject.SiteProject.constants.ErrorCodes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Getter
@EqualsAndHashCode(callSuper = true)
public class MusicNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final ErrorCodes errorCode;
    private final String details;

    public MusicNotFoundException(ErrorCodes errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
        this.details = errorCode.getMessage();
    }

    public MusicNotFoundException(ErrorCodes errorCode, String details) {
        super(details);
        this.errorCode = errorCode;
        this.details = details;
    }

}
