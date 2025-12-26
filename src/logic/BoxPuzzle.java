package logic;

import data.models.Box;
import data.repository.BoxGrid;
import exceptions.EmptyBoxException;
import exceptions.UnmovableFixedBoxException;
import tools.SpecialTool;
import enums.Letter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * BoxPuzzle
 *
 * This class manages the core game logic, including the game loop (5 turns),
 * the domino effect mechanism, and user interactions via the inner Menu class.
 *
 * It utilizes the BoxGrid to represent the 8x8 board.
 */
public class BoxPuzzle {

    // The grid containing 8x8 boxes
    private final BoxGrid boxGrid;
    
    // Tracks the coordinates of boxes moved during Stage 1.
    // Required to enforce the rule that only moved boxes can be opened in Stage 2.
    private final List<String> movedBoxesCoords; 
    
    // Scanner for user input
    private final Scanner scanner;
    
    // The target letter (A-H) that the player needs to maximize on top faces.
    private char targetLetter; 

    /**
     * Constructor initializes the grid, the tracker list, and generates a random target letter.
     */
    public BoxPuzzle() {
        this.boxGrid = new BoxGrid();
        // Using ArrayList as it provides dynamic resizing and easy access for tracking coordinates.
        this.movedBoxesCoords = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        generateTargetLetter();
    }

    /**
     * Selects a random target letter from 'A' to 'H'.
     */
    private void generateTargetLetter() {
        int randomPick = (int) (Math.random() * 8);
        this.targetLetter = (char) ('A' + randomPick);
    }

    /**
     * Starts the main game loop.
     * The game lasts for 5 turns. Each turn consists of two stages:
     * 1. Rolling (Edge selection and Domino Effect)
     * 2. Opening (Using Special Tools)
     */
    public void play() {
        System.out.println("Welcome to Box Top Side Matching Puzzle App. An 8x8 box grid is being generated.");
        System.out.println("Your goal is to maximize the letter \"" + targetLetter + "\" on the top sides of the boxes.");
        System.out.println("The initial state of the box grid:");
        System.out.println(boxGrid.toString());

        // Game lasts for 5 turns
        for (int turn = 1; turn <= 5; turn++) {
            System.out.println("\n===== > TURN " + turn + ":");
            
            // Clear the list of moved boxes at the start of each turn
            movedBoxesCoords.clear(); 
            
            Menu menu = new Menu();

            // OPTIONAL: Allow user to view box surfaces before making a move 
            if (menu.askToViewSurfaces()) {
                String loc = menu.getCoordinateInput("Please enter the location of the box you want to view: ");
                printCubeDiagram(loc);
            }

            // --- STAGE 1: ROLLING MECHANISM ---
            System.out.println("---> TURN " + turn + " FIRST STAGE:");
            try {
                runFirstStage(menu);
                // Display the grid after the roll to show changes
                System.out.println("The new state of the box grid:");
                System.out.println(boxGrid.toString());
            } catch (UnmovableFixedBoxException e) {
                // If a FixedBox is selected, the turn is wasted 
                System.out.println("TURN WASTED: " + e.getMessage());
                // Loop continues to Stage 2, but since movedBoxesCoords is empty, Stage 2 will effectively be skipped.
            } catch (Exception e) {
                System.out.println("An unexpected error occurred during Stage 1: " + e.getMessage());
            }

            // --- STAGE 2: OPENING AND TOOLS ---
            System.out.println("---> TURN " + turn + " SECOND STAGE:");
            try {
                runSecondStage(menu);
                System.out.println("The new state of the box grid:");
                System.out.println(boxGrid.toString());
            } catch (EmptyBoxException e) {
                // If an empty box is opened, the turn is wasted 
                System.out.println("TURN WASTED: " + e.getMessage());
            } catch (Exception e) {
                 System.out.println("Turn skipped: " + e.getMessage());
            }
        }

        // End of game: Calculate and print the final score
        calculateAndPrintScore();
    }

    /**
     * Executes the first stage of a turn: Rolling.
     * Handles edge selection, direction determination, and the domino effect.
     * * @param menu The menu instance to handle user input.
     * @throws UnmovableFixedBoxException if the user selects a FixedBox on the edge.
     */
    private void runFirstStage(Menu menu) throws UnmovableFixedBoxException {
        // 1. Get Edge Box Location
        String location = menu.getCoordinateInput("Please enter the location of the edge box you want to roll: ");
        int[] coords = boxGrid.parseLocation(location);
        int row = coords[0];
        int col = coords[1];

        // 2. Validate that the box is actually on the edge 
        if (!boxGrid.isEdge(row, col)) {
            System.out.println("INCORRECT INPUT: The chosen box is not on any of the edges.");
            // Recursively ask for input again until valid
            runFirstStage(menu); 
            return;
        }

        // 3. Determine Rolling Direction
        String direction;
        // If it's a corner, the user must choose the direction 
        if (boxGrid.isCorner(row, col)) {
            direction = menu.getDirectionInput(); 
        } else {
            // If it's not a corner, direction is automatically inward 
            direction = calculateAutoDirection(row, col);
        }

        // 4. Check if the selected box allows movement
        Box selectedBox = boxGrid.getBox(row, col);
        // FixedBoxes cannot be moved. This throws an exception and wastes the turn. 
        if (!selectedBox.canRoll()) { 
             throw new UnmovableFixedBoxException("Unmovable Fixed Box selected at start! Turn wasted.");
        }

        // 5. Apply the Domino Effect to roll boxes
        applyDominoEffect(row, col, direction);
    }

    /**
     * Simulates the domino effect where pushing one box rolls subsequent boxes 
     * in the same direction until a FixedBox or the grid boundary is reached.
     */
    private void applyDominoEffect(int startRow, int startCol, String direction) {
        int dRow = 0, dCol = 0;
        
        // Map direction strings to grid coordinate changes
        switch(direction.toUpperCase()) {
            case "UP": dRow = -1; break;
            case "DOWN": dRow = 1; break;
            case "LEFT": dCol = -1; break;
            case "RIGHT": dCol = 1; break;
        }

        // Safety check to prevent infinite loops if direction is invalid
        if (dRow == 0 && dCol == 0) {
            System.out.println("Error: Invalid direction. Roll cancelled.");
            return;
        }

        int currR = startRow;
        int currC = startCol;

        // Iterate through the grid in the rolling direction
        while (isValid(currR, currC)) {
            Box currentBox = boxGrid.getBox(currR, currC);

            // Domino Logic: 
            // If the current box is Fixed (doesn't allow domino) AND it is NOT the starting box,
            // the force stops here. The FixedBox itself does not move.
            if (!currentBox.allowsDomino() && (currR != startRow || currC != startCol)) {
                System.out.println("Domino effect stopped by FixedBox at R" + (currR+1) + "-C" + (currC+1));
                break;
            }

            // Perform the roll on the box object 
            currentBox.roll(direction);
            
            // Add this box to the list of moved boxes so it can be opened in Stage 2 
            movedBoxesCoords.add("R" + (currR + 1) + "-C" + (currC + 1));

            // Move to the next coordinate
            currR += dRow;
            currC += dCol;
        }
    }

    /**
     * Calculates the automatic inward direction for non-corner edge boxes.
     */
    private String calculateAutoDirection(int row, int col) {
        if (row == 0) return "DOWN";
        if (row == BoxGrid.SIZE - 1) return "UP";
        if (col == 0) return "RIGHT";
        if (col == BoxGrid.SIZE - 1) return "LEFT";
        return "RIGHT"; // Default fallback
    }

    /**
     * Checks if coordinates are within the grid boundaries.
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < BoxGrid.SIZE && c >= 0 && c < BoxGrid.SIZE;
    }

    /**
     * Executes the second stage of a turn: Opening a box and using tools.
     * * @param menu The menu instance.
     * @throws EmptyBoxException if the opened box has no tool.
     */
    private void runSecondStage(Menu menu) throws EmptyBoxException {
        // If no boxes moved in Stage 1 (due to exception or logic), skip Stage 2.
        if (movedBoxesCoords.isEmpty()) {
            System.out.println("No boxes moved in Stage 1. Skipping Stage 2.");
            return;
        }

        String location = menu.getCoordinateInput("Please enter the location of the box you want to open: ");
        
        // RULE CHECK: The player can only open a box that was rolled in the previous stage. 
        if (!movedBoxesCoords.contains(location)) {
            System.out.println("INCORRECT INPUT: The chosen box was not rolled during the first stage.");
            // Ask again recursively
            runSecondStage(menu);
            return;
        }

        Box boxToOpen = boxGrid.getBoxAt(location);
        
        // Retrieve the tool from the box. This also marks the box as open/empty.
        SpecialTool tool = boxToOpen.removeTool(); 

        // If the box is empty and contained no tool, throw exception to waste the turn. 
        if (boxToOpen.isEmpty() && tool == null) {
            throw new EmptyBoxException("The box at " + location + " is empty!");
        }

        // If a tool is found, use it immediately. 
        if (tool != null) {
            System.out.println("Box contained a SpecialTool: " + tool.getClass().getSimpleName());
            String targetLoc = menu.getCoordinateInput("Please enter location to use the tool: ");
            
            // Using polymorphism to execute the specific tool's effect
            tool.useTool(boxGrid, targetLoc, Letter.valueOf(String.valueOf(targetLetter))); 
        }
    }

    /**
     * Prints a diagram showing all surfaces of a specific box.
     * Layout matches the example in the PDF (Page 3).
     */
    private void printCubeDiagram(String location) {
        Box box = boxGrid.getBoxAt(location);
        // Assuming BoxFaces indices: 0:Top, 1:Bottom, 2:Front, 3:Back, 4:Left, 5:Right
        System.out.println("Box Surfaces for " + location + ":");
        System.out.println("   " + box.getBoxFaces().get(3)); // Back (Top in 2D view)
        System.out.print(box.getBoxFaces().get(4) + " "); // Left
        System.out.print(box.getBoxFaces().get(0) + " "); // TOP (Center)
        System.out.println(box.getBoxFaces().get(5));     // Right
        System.out.println("   " + box.getBoxFaces().get(2)); // Front (Bottom in 2D view)
        System.out.println("   " + box.getBoxFaces().get(1)); // Bottom (Hidden/Extra)
    }

    /**
     * Calculates and prints the final score (count of target letters on top faces).
     */
    private void calculateAndPrintScore() {
        int score = boxGrid.countTopLetter(Letter.valueOf(String.valueOf(targetLetter)));
        System.out.println("THE TOTAL NUMBER OF TARGET LETTER \"" + targetLetter + "\"");
        System.out.println("IN THE BOX GRID --> " + score);
        System.out.println("SUCCESS");
    }

    // ---------------------------------------------------------
    // INNER CLASS: MENU
    // ---------------------------------------------------------
    /**
     * Helper inner class to handle user input and menu interactions.
     */
    private class Menu {
        
        /**
         * Prompts user for a coordinate (e.g., R1-C1).
         * Handles validation and standardizes the output format.
         */
        public String getCoordinateInput(String msg) {
            String input = "";
            while (true) {
                System.out.print(msg);
                input = scanner.nextLine().trim().toUpperCase();
                try {
                    // Validate input format using BoxGrid's parser
                    boxGrid.parseLocation(input); 
                    
                    // Standardize input to R1-C1 format to ensure consistency with movedBoxesCoords
                    if (!input.startsWith("R")) {
                        int[] rc = boxGrid.parseLocation(input);
                        return "R" + (rc[0]+1) + "-C" + (rc[1]+1);
                    }
                    return input;
                } catch (Exception e) {
                    System.out.println("Invalid input format. Please try again (e.g. R1-C1).");
                }
            }
        }

        /**
         * Asks for rolling direction for corner boxes.
         */
        public String getDirectionInput() {
            System.out.print("The chosen box can be rolled to either [1] Right/Left or [2] Up/Down? (Enter direction name like UP/RIGHT): ");
            // In a full implementation, you might want to restrict this based on which corner it is.
            // For now, accepting direct direction string.
            return scanner.nextLine().toUpperCase();
        }

        /**
         * Asks if the user wants to view the surfaces of a box.
         */
        public boolean askToViewSurfaces() {
            System.out.print("Do you want to view all surfaces of a box? [1] Yes or [2] No? ");
            return scanner.nextLine().trim().equals("1");
        }
    }
}