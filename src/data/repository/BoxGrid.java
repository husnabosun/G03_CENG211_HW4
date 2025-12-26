package data.repository;

import data.factory.BoxFactory;
import data.models.*;
import enums.Letter;

import java.util.ArrayList;
import java.util.List;

/* * ANSWER TO COLLECTIONS QUESTION:
 * we used List<List<Box>> (NESTED ARRAY LIST) to represent the 8x8 grid.
 * This satisfies the requirement to use the Java Collections Framework,
 * keeps indexing simple, and makes printing straightforward for a fixed size board.
 * Additionally, ArrayList provides an efficient alghorithm for random access, which is
 * essential for simulating the domino effect during rolls and for tools like
 * MassRowStamp or MassColumnStamp that require fast traversal of specific rows
 * and columns.
 */


public class BoxGrid {
    public static final int SIZE = 8;
    private final List<List<Box>> grid;

    // creates the initial grid
    public BoxGrid(){
        grid = new ArrayList<>(SIZE);
        for (int rowCounter = 0; rowCounter < SIZE; rowCounter++){
            List<Box> row = new ArrayList<>(SIZE);
            for (int colCounter = 0; colCounter < SIZE; colCounter++){
                row.add(BoxFactory.createRandomBox());
            }
            grid.add(row);
        }
    }
    public Box getBox(int rowIndex, int colIndex){
        validateBounds(rowIndex, colIndex);
        return grid.get(rowIndex).get(colIndex);
    }


    public List<Box> getRow(int rowNumber) {
        return grid.get(rowNumber);
    }

    public List<Box> getColumn(int colNumber) {
        List<Box> column = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            column.add(grid.get(r).get(colNumber));
        }
        return column;
    }

    public List<Box> getFourNeighbors(int rowIndex, int colIndex) {
        validateBounds(rowIndex, colIndex);
        List<Box> neighbors = new ArrayList<>();

        // up
        if (rowIndex > 0) {
            neighbors.add(grid.get(rowIndex - 1).get(colIndex));
        }
        // down
        if (rowIndex < SIZE - 1) {
            neighbors.add(grid.get(rowIndex + 1).get(colIndex));
        }
        // left
        if (colIndex > 0) {
            neighbors.add(grid.get(rowIndex).get(colIndex - 1));
        }
        // right
        if (colIndex < SIZE - 1) {
            neighbors.add(grid.get(rowIndex).get(colIndex + 1));
        }

        return neighbors;
    }


    public void setBox(int rowIndex, int colIndex, Box box) {
        validateBounds(rowIndex, colIndex);
        grid.get(rowIndex).set(colIndex, box);
    }

    public boolean isEdge(int rowIndex, int colIndex){
        validateBounds(rowIndex, colIndex);
        return (rowIndex == 0 || rowIndex == SIZE - 1 || colIndex == 0 || colIndex == SIZE - 1 );
    }

    // options : top-right, bottom-right, top-left, top-right
    public boolean isCorner(int rowIndex, int colIndex){
        validateBounds(rowIndex, colIndex);
        return ((rowIndex == 0) || rowIndex == SIZE - 1) && (colIndex == 0 || colIndex == SIZE - 1);
    }


    public int countTopLetter(Letter letterToCount){
        int count = 0;
        for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            for (int colIndex = 0; colIndex < SIZE; colIndex++) {
                if (getBox(rowIndex, colIndex).getTopFace() == letterToCount) {
                    count++;
                }
            }
        }
        return count;
    }

    public int[] parseLocation(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        String fixedString = input.trim().toUpperCase();

        int row;
        int col;

        // Format: R2-C4
        if (fixedString.startsWith("R") && fixedString.contains("-C")) {

            int dashIndex = fixedString.indexOf('-');

            // R2 part
            String rowPart = fixedString.substring(1, dashIndex);
            // C4 part
            String colPart = fixedString.substring(dashIndex + 2);

            row = Integer.parseInt(rowPart) - 1;
            col = Integer.parseInt(colPart) - 1;
        }
        // Format: 2-4
        else if (fixedString.contains("-")) {

            String[] parts = fixedString.split("-");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid location format");
            }

            row = Integer.parseInt(parts[0]) - 1;
            col = Integer.parseInt(parts[1]) - 1;
        }
        else {
            throw new IllegalArgumentException("Invalid location format");
        }

        // boundary check
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException(
                    "Out of bounds: R" + (row + 1) + "-C" + (col + 1)
            );
        }

        return new int[]{row, col};
    }

    public Box getBoxAt(String location){
        int[] coordinates = parseLocation(location);
        // coordinates are returned as [row,column]
        return getBox(coordinates[0], coordinates[1]);
    }

    private void validateBounds(int rowIndex, int colIndex) {
        if (rowIndex < 0 || rowIndex >= SIZE || colIndex < 0 || colIndex >= SIZE) {
            throw new IndexOutOfBoundsException("Out of bounds: R" + (rowIndex + 1) + "-C" + (colIndex + 1));
        }
    }

    // Printing helpers
    private char typeChar(Box box) {
        return box.getBoxTypeMarker();
    }

    private char statusChar(Box box) {
        return box.getStatusMarker();
    }

    private String cellString(Box box) {
        String type = String.valueOf(box.getBoxTypeMarker());
        String face = box.getTopFace().toString();

        if (type.equals("X")) {
            return String.format(" %s-%s-  |", type, face);
        }

        // Show status marker for others
        char status = box.getStatusMarker(); // M or O
        return String.format(" %s-%s-%s |", type, face, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Column headers (C1, C2...)
        sb.append("\n        ");
        for (int i = 1; i <= SIZE; i++) {
            sb.append(String.format("C%-7d", i));
        }
        sb.append("\n    ");
        // Top line
        for (int i = 0; i < SIZE * 8 + 1; i++) sb.append("-");
        sb.append("\n");

        // Rows
        for (int i = 0; i < SIZE; i++) {
            sb.append(String.format("R%-3d|", (i + 1)));
            for (int j = 0; j < SIZE; j++) {
                sb.append(cellString(grid.get(i).get(j)));
            }
            sb.append("\n    ");
            // Seperating line
            for (int k = 0; k < SIZE * 8 + 1; k++) sb.append("-");
            sb.append("\n");
        }
        return sb.toString();
    }
}
