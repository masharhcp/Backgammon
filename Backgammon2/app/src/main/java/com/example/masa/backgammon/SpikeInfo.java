package com.example.masa.backgammon;

import java.io.Serializable;

public class SpikeInfo implements Serializable {
    int num;
    int numOfChips;
    int chipColor;

    public SpikeInfo(int num, int numOfChips, int chipColor){
        this.chipColor = chipColor;
        this.num = num;
        this.numOfChips = numOfChips;

    }

    public SpikeInfo(){


    }

    public void decrementNumberOfChps(){ numOfChips = numOfChips-1;}
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNumOfChips() {
        return numOfChips;
    }

    public void setNumOfChips(int numOfChips) {
        this.numOfChips = numOfChips;
    }

    public int getChipColor() {
        return chipColor;
    }

    public void setChipColor(int chipColor) {
        this.chipColor = chipColor;
    }

    public void changeColor (){
        if (chipColor == 0) chipColor = 1;
        else chipColor = 0;
    }
}
