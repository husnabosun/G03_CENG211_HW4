package data.models;

import enums.Letter;

import java.util.List;

public class FixedBox extends Box{
    public FixedBox(List<Letter> faces){
        super(faces);
        this.isEmpty = true;
    }
    @Override
    public boolean canRoll(){
        return false;
    }

    @Override
    public boolean allowsDomino(){
        return false;
    }

    @Override
    public void restamp(Letter newLetter){
        // Do nothing. The top letter does not change in Fixed Box
    }

    @Override
    public char getBoxTypeMarker() {
        return 'X';
    }

}
