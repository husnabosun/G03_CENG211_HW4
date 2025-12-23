package data.models;

import enums.Letter;

import java.util.List;

public class UnchangingBox extends Box{
    public UnchangingBox(List<Letter> faces){
        super(faces);
    }

    @Override
    public boolean canRoll(){
        return true;
    }

    @Override
    public boolean allowsDomino(){
        return true;
    }

    @Override
    public void restamp(Letter newLetter){
        // Do nothing. The top letter does not change in Unchanging box
    }

    @Override
    public char getBoxTypeMarker() {
        return 'U';
    }
}
