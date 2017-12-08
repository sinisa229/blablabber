package com.blablabber;

public class BlablabberException extends RuntimeException {

    public BlablabberException() {
        super();
    }

    public BlablabberException(String message) {
        super(message);
    }

    public BlablabberException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlablabberException(Throwable cause) {
        super(cause);
    }

    public BlablabberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
