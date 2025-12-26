package exceptions;

public class EmptyBoxException extends Exception {
    /**
     * Exception thrown when a player opens a box that contains no SpecialTool.
     * This wastes the turn.
     */
    public EmptyBoxException(String message) {
        super(message);
    }

    public EmptyBoxException() {
        super("BOX IS EMPTY! Continuing to the next turn...");
    }
}