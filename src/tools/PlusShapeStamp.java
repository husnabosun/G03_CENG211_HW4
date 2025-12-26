package tools;

import data.models.Box;
import data.repository.BoxGrid;
import enums.Letter;
import java.util.List;

/**
 * PlusShapeStamp: Re-stamps 5 boxes in a plus shape to the target letter.
 * The center box and its 4 neighbors (up, down, left, right) are stamped.
 */
public class PlusShapeStamp extends SpecialTool {

    @Override
    public void useTool(BoxGrid grid, String location, Letter targetLetter) {
        try {
            int[] coords = grid.parseLocation(location);
            int row = coords[0];
            int col = coords[1];

            // Stamp the center box
            Box centerBox = grid.getBox(row, col);
            centerBox.restamp(targetLetter);

            // Get and stamp the 4 neighbors
            List<Box> neighbors = grid.getFourNeighbors(row, col);
            for (Box neighbor : neighbors) {
                neighbor.restamp(targetLetter);
            }

            System.out.println("Top sides of the chosen box (" + location + ") and its surrounding boxes have been stamped to letter \"" + targetLetter + "\".");

        } catch (Exception e) {
            System.out.println("Error using PlusShapeStamp: " + e.getMessage());
        }
    }
}