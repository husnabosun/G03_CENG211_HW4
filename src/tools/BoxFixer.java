package tools;

import data.factory.FaceFactory;
import data.models.Box;
import data.models.FixedBox;
import data.repository.BoxGrid;
import enums.Letter;
import exceptions.BoxAlreadyFixedException;

/**
 * BoxFixer: Replaces a box with an identical FixedBox copy.
 * If the box has a tool inside, it is removed from the game.
 * Cannot fix a box that is already a FixedBox - throws BoxAlreadyFixedException.
 */
public class BoxFixer extends SpecialTool {

    @Override
    public void useTool(BoxGrid grid, String location, Letter targetLetter) {
        try {
            int[] coords = grid.parseLocation(location);
            int row = coords[0];
            int col = coords[1];

            Box originalBox = grid.getBox(row, col);

            // Check if already a FixedBox
            if (originalBox instanceof FixedBox) {
                throw new BoxAlreadyFixedException("Cannot fix a box that is already fixed! Turn wasted.");
            }

            // Create a new FixedBox with the same faces as the original box
            FixedBox fixedBox = new FixedBox(originalBox.getBoxFaces());

            // Replace the box in the grid
            grid.setBox(row, col, fixedBox);

            System.out.println("The box at location " + location + " has been replaced with a FixedBox.");

        } catch (BoxAlreadyFixedException e) {
            System.out.println("TURN WASTED: " + e.getMessage());
            throw new RuntimeException(e); // Re-throw to be caught in BoxPuzzle
        } catch (Exception e) {
            System.out.println("Error using BoxFixer: " + e.getMessage());
        }
    }
}