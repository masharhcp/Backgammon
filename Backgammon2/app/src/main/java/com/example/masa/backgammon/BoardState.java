package com.example.masa.backgammon;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardState implements Serializable {
    ArrayList<SpikeInfo> spikeInfo = new ArrayList<>();
    ArrayList<BarInfo> barInfo = new ArrayList<>();
    boolean movingChip;
    int chipX, chipY, movingChipColor;
    int movingChipFromSpike;



    public BoardState() {
        this.spikeInfo = null;
        this.barInfo = null;
        movingChip = false;
        movingChipColor = -1;
        movingChipFromSpike = -1;

    }

    public ArrayList<BarInfo> getBarInfo() {
        return barInfo;
    }

    public void setBarInfo(ArrayList<BarInfo> barInfo) {
        this.barInfo = barInfo;
    }

    public ArrayList<SpikeInfo> getSpikeInfo() {
        return spikeInfo;
    }

    public void setSpikeInfo(ArrayList<SpikeInfo> spikeInfo) {
        this.spikeInfo = spikeInfo;
    }

    public boolean isMovingChip() {
        return movingChip;
    }

    public void setMovingChip(boolean movingChip) {
        this.movingChip = movingChip;
    }

    public int getChipX() {
        return chipX;
    }

    public void setChipX(int chipX) {
        this.chipX = chipX;
    }

    public int getChipY() {
        return chipY;
    }

    public void setChipY(int chipY) {
        this.chipY = chipY;
    }

    public int getMovingChipColor() {
        return movingChipColor;
    }

    public void setMovingChipColor(int movingChipColor) {
        this.movingChipColor = movingChipColor;
    }

    public int getMovingChipFromSpike() {
        return movingChipFromSpike;
    }

    public void setMovingChipFromSpike(int movingChipFromSpike) {
        this.movingChipFromSpike = movingChipFromSpike;
    }
}
