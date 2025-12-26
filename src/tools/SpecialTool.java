package tools;

import data.repository.BoxGrid;
import enums.Letter;

/**
 * Abstract base class for all special tools.
 * Uses generics to allow polymorphic usage in BoxPuzzle.
 */
public abstract class SpecialTool {

    /**
     * Uses the tool on the grid at the specified location with the target letter.
     *
     * @param grid The BoxGrid to apply the tool on
     * @param location The location string (e.g., "R2-C4")
     * @param targetLetter The target letter to stamp boxes with
     */
    public abstract void useTool(BoxGrid grid, String location, Letter targetLetter);

    /**
     * Returns the name of the tool for display purposes.
     */
    public String getToolName() {
        return this.getClass().getSimpleName();
    }
}