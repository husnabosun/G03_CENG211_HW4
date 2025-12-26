package data.models;

import enums.Letter;
import enums.Direction;
import tools.SpecialTool;
import java.util.List;

public abstract class Box {

    protected List<Letter> boxFaces;
    protected boolean isOpened;
    protected boolean isEmpty;
    protected SpecialTool specialTool;

    public Box(List<Letter> boxFaces){
        this.boxFaces = boxFaces;
        this.isOpened = false;
        this.isEmpty = false;
        this.specialTool = null;
    }

    public Letter getTopFace() {
        return boxFaces.get(0);
    }

    public SpecialTool removeTool() {
        if (specialTool == null) {
            return null;
        }

        SpecialTool temp = specialTool;
        specialTool = null;
        isEmpty = true;
        isOpened = true;
        return temp;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
    public SpecialTool getTool(){
        return specialTool;
    }


    public boolean hasTool() {
        return specialTool != null;
    }


    public void setTool(SpecialTool specialTool) {
        this.specialTool = specialTool;
        if(specialTool == null){
            this.isEmpty = true;
        }
    }

    public List<Letter> getBoxFaces(){
        return boxFaces;
    }

    public char getStatusMarker(){
        if (isEmpty){
            return 'O';
        }
        else{
           return 'M';
        }
    }

    public void flip() {
        // switches the up(index=0) and down(index=1) faces
        Letter upFace = boxFaces.get(0);
        Letter downFace = boxFaces.get(1);

        boxFaces.set(0, downFace);
        boxFaces.set(1, upFace);
    }

    /**
     * Rolls the box in the specified direction, simulating dice rolling.
     * Updates the box faces accordingly.
     *
     * Face indices: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right
     */
    public void roll(Direction direction) {
        Letter temp;

        switch (direction) {
            case RIGHT:
                // Top -> Right, Right -> Bottom, Bottom -> Left, Left -> Top
                temp = boxFaces.get(0);
                boxFaces.set(0, boxFaces.get(4));  // Left becomes Top
                boxFaces.set(4, boxFaces.get(1));  // Bottom becomes Left
                boxFaces.set(1, boxFaces.get(5));  // Right becomes Bottom
                boxFaces.set(5, temp);             // Top becomes Right
                break;

            case LEFT:
                // Top -> Left, Left -> Bottom, Bottom -> Right, Right -> Top
                temp = boxFaces.get(0);
                boxFaces.set(0, boxFaces.get(5));  // Right becomes Top
                boxFaces.set(5, boxFaces.get(1));  // Bottom becomes Right
                boxFaces.set(1, boxFaces.get(4));  // Left becomes Bottom
                boxFaces.set(4, temp);             // Top becomes Left
                break;

            case UP:
                // Top -> Back, Back -> Bottom, Bottom -> Front, Front -> Top
                temp = boxFaces.get(0);
                boxFaces.set(0, boxFaces.get(2));  // Front becomes Top
                boxFaces.set(2, boxFaces.get(1));  // Bottom becomes Front
                boxFaces.set(1, boxFaces.get(3));  // Back becomes Bottom
                boxFaces.set(3, temp);             // Top becomes Back
                break;

            case DOWN:
                // Top -> Front, Front -> Bottom, Bottom -> Back, Back -> Top
                temp = boxFaces.get(0);
                boxFaces.set(0, boxFaces.get(3));  // Back becomes Top
                boxFaces.set(3, boxFaces.get(1));  // Bottom becomes Back
                boxFaces.set(1, boxFaces.get(2));  // Front becomes Bottom
                boxFaces.set(2, temp);             // Top becomes Front
                break;

            default:
                System.out.println("Invalid roll direction: " + direction);
        }
    }

    public abstract boolean canRoll();

    public abstract boolean allowsDomino();

    public abstract void restamp(Letter newLetter);

    public abstract char getBoxTypeMarker();

}
