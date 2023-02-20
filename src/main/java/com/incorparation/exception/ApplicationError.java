package com.incorparation.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationError {
    private String errorMessage;
    private Integer errorCode;

    public ApplicationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
