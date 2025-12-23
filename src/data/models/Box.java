package data.models;

import enums.Letter;
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

    public abstract boolean canRoll();

    public abstract boolean allowsDomino();

    public abstract void restamp(Letter newLetter);

    public abstract char getBoxTypeMarker();

}
