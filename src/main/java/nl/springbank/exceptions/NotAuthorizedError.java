package nl.springbank.exceptions;

/**
 * The user is not authorized to take this action.
 *
 * @author Tristan de Boer.
 */
public class NotAuthorizedError extends Exception {
    public NotAuthorizedError(String message) {
        super(message);
    }
}
