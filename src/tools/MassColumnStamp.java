package tools;

import data.models.Box;
import data.repository.BoxGrid;
import enums.Letter;
import java.util.List;

/**
 * MassColumnStamp: Re-stamps all boxes in an entire column to the target letter.
 */
public class MassColumnStamp extends SpecialTool {

    @Override
    public void useTool(BoxGrid grid, String location, Letter targetLetter) {
        try {
            int[] coords = grid.parseLocation(location);
            int colNumber = coords[1];

            // Get the entire column
            List<Box> column = grid.getColumn(colNumber);

            // Stamp all boxes in the column
            for (Box box : column) {
                box.restamp(targetLetter);
            }

            System.out.println("All boxes in column C" + (colNumber + 1) + " have been stamped to letter \"" + targetLetter + "\".");

        } catch (Exception e) {
            System.out.println("Error using MassColumnStamp: " + e.getMessage());
        }
    }
}