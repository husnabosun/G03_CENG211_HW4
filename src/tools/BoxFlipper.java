package tools;

import data.models.Box;
import data.models.FixedBox;
import data.repository.BoxGrid;
import enums.Letter;
import exceptions.UnmovableFixedBoxException;

/**
 * BoxFlipper: Flips a box upside down.
 * The top side becomes the bottom side and vice versa.
 * Cannot flip FixedBoxes - throws UnmovableFixedBoxException.
 */
public class BoxFlipper extends SpecialTool {

    @Override
    public void useTool(BoxGrid grid, String location, Letter targetLetter) {
        try {
            int[] coords = grid.parseLocation(location);
            int row = coords[0];
            int col = coords[1];

            Box box = grid.getBox(row, col);

            // Check if the box is a FixedBox
            if (box instanceof FixedBox) {
                throw new UnmovableFixedBoxException("Cannot flip a FixedBox! Turn wasted.");
            }

            // Flip the box
            box.flip();

            System.out.println("The chosen box on location " + location + " has been flipped upside down.");

        } catch (UnmovableFixedBoxException e) {
            System.out.println("TURN WASTED: " + e.getMessage());
            throw new RuntimeException(e); // Re-throw to be caught in BoxPuzzle
        } catch (Exception e) {
            System.out.println("Error using BoxFlipper: " + e.getMessage());
        }
    }
}