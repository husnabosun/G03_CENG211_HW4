package app;

import logic.BoxPuzzle;

/**
 * BoxPuzzleApp
 *
 * The main entry point for the "Box Top Side Matching Puzzle App".
 * It is responsible for initializing the BoxPuzzle game logic and starting the game loop.
 */
public class BoxPuzzleApp {

    public static void main(String[] args) {
        // Initialize the main game object as requested in the assignment guidelines.
        BoxPuzzle puzzle = new BoxPuzzle();
        
        // Start the game logic.
        puzzle.play();
    }
}