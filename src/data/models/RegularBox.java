package data.models;

import enums.Letter;

import java.util.List;

public class RegularBox extends Box{
    public RegularBox(List<Letter> faces){
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
        boxFaces.set(0, newLetter);
    }

    @Override
    public char getBoxTypeMarker() {
        return 'R';
    }
}
