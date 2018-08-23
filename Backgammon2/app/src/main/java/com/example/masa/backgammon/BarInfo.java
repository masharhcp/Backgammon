package com.example.masa.backgammon;

import java.io.Serializable;

class BarInfo implements Serializable{

    int barColor;
    int numOfChips;

    public BarInfo(int barColor, int numOfChips) {
        this.barColor = barColor;
        this.numOfChips = numOfChips;
    }


    public void incrementChips (){ numOfChips = numOfChips+1;}
    public void decrementChips (){ numOfChips = numOfChips-1;}
    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getNumOfChips() {
        return numOfChips;
    }

    public void setNumOfChips(int numOfChips) {
        this.numOfChips = numOfChips;
    }
}
