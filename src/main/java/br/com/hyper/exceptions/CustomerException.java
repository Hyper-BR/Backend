package br.com.hyper.exceptions;

import br.com.hyper.constants.ErrorCodes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Getter
public class CustomerException extends RuntimeException{

    private final ErrorCodes errorCodes;
    private final String details;

    public CustomerException(ErrorCodes errorCode, Exception e){
        super(e);
        this.errorCodes = errorCode;
        this.details = errorCode.getMessage();
    }

    public CustomerException(ErrorCodes errorCode, String details){
        super(details);
        this.errorCodes = errorCode;
        this.details = details;
    }
}
