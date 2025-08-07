package br.com.hyper.exceptions;

import br.com.hyper.enums.ErrorCodes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Getter
@EqualsAndHashCode(callSuper = true)
public class GenericException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final ErrorCodes errorCode;
    private final String details;

    public GenericException(ErrorCodes errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
        this.details = errorCode.getMessage();
    }

    public GenericException(ErrorCodes errorCode, String details) {
        super(details);
        this.errorCode = errorCode;
        this.details = details;
    }

}
