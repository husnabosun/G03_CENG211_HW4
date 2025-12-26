package exceptions;

public class BoxAlreadyFixedException extends Exception {
    /**
     * Exception thrown when trying to fix a FixedBox using BoxFixer tool.
     * This wastes the turn.
     */
    public BoxAlreadyFixedException(String message) {
        super(message);
    }

    public BoxAlreadyFixedException() {
        super("Cannot fix a box that is already fixed!");
    }
}