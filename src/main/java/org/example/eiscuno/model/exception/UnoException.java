package org.example.eiscuno.model.exception;

/**
 * Custom exception class for UNO game-related exceptions.
 * <p>
 * This exception is used to handle specific exceptions related to the UNO game logic.
 * </p>
 */
public class UnoException extends Exception {

    /**
     * Constructs a new UnoException with a default error message.
     * The default message is: "This is a custom exception message."
     */
    public UnoException() {
        super("This is a custom exception message.");
    }

    /**
     * Constructs a new UnoException with the specified error message.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method)
     */
    public UnoException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnoException with the specified error message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method)
     * @param cause   the cause (which is saved for later retrieval by the Throwable.getCause() method)
     */
    public UnoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new UnoException with the specified cause.
     *
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method)
     */
    public UnoException(Throwable cause) {
        super(cause);
    }
}
