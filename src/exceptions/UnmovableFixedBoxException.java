package exceptions;

public class UnmovableFixedBoxException extends Exception {
    /**
     * Exception thrown when:
     * 1. An edge FixedBox is selected during the first stage of a turn
     * 2. A FixedBox is attempted to be flipped using BoxFlipper
     * This wastes the turn.
     */
    public UnmovableFixedBoxException(String message) {
        super(message);
    }

    public UnmovableFixedBoxException() {
        super("HOWEVER, IT IS FIXED BOX AND CANNOT BE MOVED. Continuing to the next turn...");
    }
}