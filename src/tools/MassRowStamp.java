package tools;

import data.models.Box;
import data.repository.BoxGrid;
import enums.Letter;
import java.util.List;

/**
 * MassRowStamp: Re-stamps all boxes in an entire row to the target letter.
 */
public class MassRowStamp extends SpecialTool {

    @Override
    public void useTool(BoxGrid grid, String location, Letter targetLetter) {
        try {
            int[] coords = grid.parseLocation(location);
            int rowNumber = coords[0];

            // Get the entire row
            List<Box> row = grid.getRow(rowNumber);

            // Stamp all boxes in the row
            for (Box box : row) {
                box.restamp(targetLetter);
            }

            System.out.println("All boxes in row R" + (rowNumber + 1) + " have been stamped to letter \"" + targetLetter + "\".");

        } catch (Exception e) {
            System.out.println("Error using MassRowStamp: " + e.getMessage());
        }
    }
}