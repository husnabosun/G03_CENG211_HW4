package data.repository;

import data.factory.BoxFactory;
import data.models.*;
import enums.Letter;

import java.util.ArrayList;
import java.util.List;

/*
ANSWER TO COLLECTIONS QUESTION:
I used List<List<Box>> (NESTED ARRAY LIST) to represent the 8x8 grid.
This satisfies the requirement to use the Java Collections Framework, keeps indexing simple
and makes printing straightforward for a fixed  size board.
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

    // TODO: yagmur will need to change whole ROW in MassRowStamp
    // TODO: and to change whole COLUMN in MassColumnStamp

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

    // TODO: yagmur will need to receive 4 neighbours of a box
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
        return typeChar(box) + "-" + box.getTopFace() + "-" + statusChar(box);
    }

    @Override
    public String toString() {
        String result = "";

        // Header
        result += "     ";
        for (int c = 1; c <= SIZE; c++) {
            result += "   C" + c + "    ";
        }
        result += "\n";

        // Separator
        result += "   ";
        for (int c = 0; c < SIZE; c++) {
            result += "---------";
        }
        result += "\n";

        // Rows
        for (int r = 0; r < SIZE; r++) {

            result += "R" + (r + 1) + " ";

            for (int c = 0; c < SIZE; c++) {
                result += "| " + cellString(getBox(r, c)) + " ";
            }
            result += "|\n";

            result += "   ";
            for (int c = 0; c < SIZE; c++) {
                result += "---------";
            }
            result += "\n";
        }

        return result;
    }



}
