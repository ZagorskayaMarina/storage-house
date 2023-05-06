package com.incorparation.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationError {
    private String errorMessage;
    private Integer httpErrorCode;

    public ApplicationError(String errorMessage, Integer httpErrorCode) {
        this.errorMessage = errorMessage;
        this.httpErrorCode = httpErrorCode;
    }
}
