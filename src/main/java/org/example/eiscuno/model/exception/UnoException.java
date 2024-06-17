package org.example.eiscuno.model.exception;

public class UnoException extends Exception {
    public UnoException() {
        super("This is a custom exception message.");
    }

    public UnoException(String message) {
        super(message);
    }

    public UnoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnoException(Throwable cause) {
        super(cause);
    }
}
