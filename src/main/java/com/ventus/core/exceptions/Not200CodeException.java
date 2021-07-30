package com.ventus.core.exceptions;

import lombok.Getter;
import lombok.Setter;

public class Not200CodeException extends Exception {

    @Setter
    @Getter
    private int errorCode;

    public Not200CodeException(int responseCode) {
        super("Not 200 response [" + responseCode + "]");
        errorCode = responseCode;
    }
}
