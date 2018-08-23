package com.example.masa.backgammon;

import java.io.Serializable;
import java.util.ArrayList;

public class Move implements Serializable {
    int fromSpike;
    ArrayList<Integer> toSpikes;
    boolean mustMoveFromBar;

    public Move(int fromSpike) {
        this.fromSpike = fromSpike;
        toSpikes = new ArrayList<>();

    }

    public boolean isMustMoveFromBar() {
        return mustMoveFromBar;
    }

    public void setMustMoveFromBar(boolean mustMoveFromBar) {
        this.mustMoveFromBar = mustMoveFromBar;
    }

    public int getFromSpike() {
        return fromSpike;
    }

    public void setFromSpike(int fromSpike) {
        this.fromSpike = fromSpike;
        this.mustMoveFromBar = false;
    }

    public ArrayList<Integer> getToSpikes() {
        return toSpikes;
    }

    public void setToSpikes(ArrayList<Integer> toSpikes) {
        this.toSpikes = toSpikes;
    }


    public void  addToSpikes (int a){
        toSpikes.add(a);
    }
}
