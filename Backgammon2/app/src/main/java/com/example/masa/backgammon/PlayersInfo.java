package com.example.masa.backgammon;

import java.io.Serializable;

public class PlayersInfo implements Serializable{
    private String whitePlayerName;
    private String blackPlayerName;
    private int comp;
    // 0 je white; 1 je black
    private int turn;
    private int diceOne, diceTwo;
    private boolean moveEnded = true;
    private int moveState;
    private boolean diceOnePlayed, diceTwoPlayed;
    private int playerWon = -1;
    private boolean canRollDice = true;
    private boolean compNoMoves = false;


    public boolean isCanRollDice() {
        return canRollDice;
    }


    public boolean isCompNoMoves() {
        return compNoMoves;
    }

    public void setCompNoMoves(boolean compNoMoves) {
        this.compNoMoves = compNoMoves;
    }

    public boolean canRollDice() {
        return canRollDice;
    }

    public void setCanRollDice(boolean canRollDice) {
        this.canRollDice = canRollDice;
    }

    public int getPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(int playerWon) {
        this.playerWon = playerWon;
    }

    public boolean isDiceOnePlayed() {
        return diceOnePlayed;
    }

    public void setDiceOnePlayed(boolean diceOnePlayed) {
        this.diceOnePlayed = diceOnePlayed;
    }

    public boolean isDiceTwoPlayed() {
        return diceTwoPlayed;
    }

    public void setDiceTwoPlayed(boolean diceTwoPlayed) {
        this.diceTwoPlayed = diceTwoPlayed;
    }

    public boolean isMoveEnded() {
        return moveEnded;
    }

    public int getMoveState() {
        return moveState;
    }

    public void setMoveState(int moveState) {
        this.moveState = moveState;
    }

    public void setMoveEnded(boolean moveEnded) {
        this.moveEnded = moveEnded;
    }

    public PlayersInfo() {
    }

    public int getDiceOne() {
        return diceOne;
    }

    public void setDiceOne(int diceOne) {
        if (diceOne >=-1 && diceOne < 7)
        this.diceOne = diceOne;
    }

    public int getDiceTwo() {
        return diceTwo;
    }

    public void setDiceTwo(int diceTwo) {
        if (diceTwo >=-1 && diceTwo < 7)
        this.diceTwo = diceTwo;
    }

    public PlayersInfo(String whitePlayerName, String blackPlayerName, int comp) {
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        this.comp = comp;
        this.turn  = 0;
        this.diceOne = 1;
        this.diceTwo = 1;
        this.moveState =  0;
    }


    public void changeTurn(){
        if (turn == 0){ turn = 1;
        }else
            if(turn ==1){turn = 0;}
    }
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public void setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public void setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
    }

    public int getComp() {
        return comp;
    }

    public void setComp(int comp) {
        this.comp = comp;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean sameDice(){
        return diceOne == diceTwo;
    }
}
