package com.example.masa.backgammon;

import android.content.Intent;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GameModel implements Serializable {

    private BoardState boardState;
    private PlayersInfo playersInfo;
    private ArrayList<Move> moves = new ArrayList<>();
    private ArrayList<Integer> spikesToShine = new ArrayList<>();
    private ThinkingDots tDots;


    public ThinkingDots getDots() {
        return tDots;
    }

    public void setDots(ThinkingDots tDots) {
        this.tDots = tDots;
    }

    public ArrayList<Integer> getSpikesToShine() {
        return spikesToShine;
    }

    public void setSpikesToShine(ArrayList<Integer> spikesToShine) {
        this.spikesToShine = spikesToShine;
    }

    public PlayersInfo getPlayersInfo() {
        return playersInfo;
    }

    public void setPlayersInfo(PlayersInfo playersInfo) {
        this.playersInfo = playersInfo;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    public BoardState grabChip(int r) {

        boolean not2526 = false;
        boolean validSpike = false;
        if (r != -1 && playersInfo.getMoveState() != 2) {

            for (Move m : moves) {
                if (m.fromSpike == 25 || m.fromSpike == 26) {
                    if (m.fromSpike == r + 1 || m.fromSpike == r + 2) {
                        validSpike = true;
                        spikesToShine.clear();
                        for (int i : m.toSpikes) {
                            spikesToShine.add(i);
                        }


                        if (playersInfo.getTurn() == 0 && boardState.getBarInfo().get(0).numOfChips > 0) {
                            boardState.movingChipFromSpike = r;
                            SpikeInfo spikeInfoR = this.boardState.spikeInfo.get(r);
                            boardState.spikeInfo.get(r).decrementNumberOfChps();
                            boardState.getBarInfo().get(0).decrementChips();
                            boardState.setMovingChip(true);
                            boardState.setMovingChipColor(spikeInfoR.chipColor);

                        } else if (playersInfo.getTurn() == 1 && boardState.getBarInfo().get(1).numOfChips > 0) {
                            boardState.movingChipFromSpike = r;
                            SpikeInfo spikeInfoR = this.boardState.spikeInfo.get(r);
                            boardState.spikeInfo.get(r).decrementNumberOfChps();
                            boardState.getBarInfo().get(1).decrementChips();
                            boardState.setMovingChip(true);
                            boardState.setMovingChipColor(spikeInfoR.chipColor);

                        }
                    }

                } else {

                    if (m.fromSpike == boardState.getSpikeInfo().get(r).num) {
                        validSpike = true;
                        spikesToShine.clear();
                        for (int i : m.toSpikes) {
                            spikesToShine.add(i);
                        }
                        not2526 = true;
                    }


                }
            }

        }

        if (not2526) {
            if (boardState.spikeInfo.get(r).getNumOfChips() > 0 && validSpike) {
                int chips = boardState.spikeInfo.get(r).getNumOfChips();
                boardState.movingChipFromSpike = r;
                SpikeInfo spikeInfoR = this.boardState.spikeInfo.get(r);
                spikeInfoR.setNumOfChips(chips - 1);
                boardState.setMovingChipColor(spikeInfoR.chipColor);
                if (spikeInfoR.getNumOfChips() == 0) {
                    spikeInfoR.setChipColor(-1);
                }

                boardState.setMovingChip(true);

            }
        }

        if (playersInfo.getMoveState() == 2) {
            playersInfo.setCanRollDice(true);
        }
        return boardState;

    }

    public BoardState dropChip(int r) {


        if (r != -1) {
            if (boardState.isMovingChip()) {
                if ((boardState.spikeInfo.get(r).getNumOfChips() == 0 || boardState.spikeInfo.get(r).getChipColor() == boardState.getMovingChipColor()) && canDropOnSpike(boardState.spikeInfo.get(r).getNum())) {
                    determentDice(r);


                    int chips = boardState.spikeInfo.get(r).getNumOfChips();
                    if (boardState.spikeInfo.get(r).getNumOfChips() == 0) {
                        boardState.spikeInfo.get(r).setChipColor(boardState.getMovingChipColor());
                    }
                    boardState.spikeInfo.get(r).setNumOfChips(++chips);

                    boardState.setMovingChip(false);
                    spikesToShine.clear();
                    tDots.updateBoard();
                    setMoveState();
                    if (playersInfo.getPlayerWon() == -1)
                        tDots.updateBoard();
                    else{
                        //return null;
                    }


                } else {

                    if (boardState.spikeInfo.get(r).getNumOfChips() == 1 && boardState.spikeInfo.get(r).getChipColor() != playersInfo.getTurn() && canDropOnSpike(boardState.spikeInfo.get(r).getNum())) {
                        if (playersInfo.getTurn() == 0) {
                            boardState.getBarInfo().get(1).numOfChips++;
                            boardState.spikeInfo.get(r).changeColor();
                            determentDice(r);
                            //setMoveState();


                        } else {
                            boardState.getBarInfo().get(0).numOfChips++;
                            boardState.spikeInfo.get(r).changeColor();
                            determentDice(r);
                        }
                        boardState.setMovingChip(false);
                        spikesToShine.clear();
                        tDots.updateBoard();
                        setMoveState();
                        tDots.updateBoard();


                    } else {
                        int chips = boardState.spikeInfo.get(boardState.movingChipFromSpike).getNumOfChips();
                        if (boardState.spikeInfo.get(boardState.movingChipFromSpike).getNumOfChips() == 0) {
                            boardState.spikeInfo.get(boardState.movingChipFromSpike).setChipColor(boardState.getMovingChipColor());
                        }
                        boardState.spikeInfo.get(boardState.movingChipFromSpike).setNumOfChips(chips + 1);
                        boardState.setMovingChip(false);
                        // spikesToShine.clear();
                        if (boardState.movingChipFromSpike == 24) {
                            boardState.getBarInfo().get(0).incrementChips();
                        }
                        if (boardState.movingChipFromSpike == 25) {
                            boardState.getBarInfo().get(1).incrementChips();
                        }
                        spikesToShine.clear();
                        if (playersInfo.getMoveState() != 2) {
                            checkForMoves();
                            if (moves.isEmpty() && !playersInfo.sameDice()) {
//                                playersInfo.changeTurn();
//                                playersInfo.setCanRollDice(true);
//                                playersInfo.setMoveState(0);
//                                playCompMove();
                                if (playersInfo.getTurn() == playersInfo.getComp())
                                    playersInfo.setMoveState(3);
                                else
                                    playersInfo.setMoveState(4);
                                tDots.updateBoard();
                                setMoveState();
                                tDots.updateBoard();


                            }


                        }

                    }

                }
            }
        } else {
            if (boardState.isMovingChip()) {
                int chips = boardState.spikeInfo.get(boardState.movingChipFromSpike).getNumOfChips();
                if (boardState.spikeInfo.get(boardState.movingChipFromSpike).getNumOfChips() == 0) {
                    boardState.spikeInfo.get(boardState.movingChipFromSpike).setChipColor(boardState.getMovingChipColor());
                }
                boardState.spikeInfo.get(boardState.movingChipFromSpike).setNumOfChips(chips + 1);
                boardState.setMovingChip(false);
                //spikesToShine.clear();

                //returnChipToBar();
                if (boardState.movingChipFromSpike == 24) {
                    boardState.getBarInfo().get(0).incrementChips();
                }
                if (boardState.movingChipFromSpike == 25) {
                    boardState.getBarInfo().get(1).incrementChips();
                }
                spikesToShine.clear();
                if (playersInfo.getMoveState() != 2) {
                    checkForMoves();
                    if (moves.isEmpty() && !playersInfo.sameDice()) {
//                        playersInfo.changeTurn();
//                        playersInfo.setCanRollDice(true);
//                        playersInfo.setMoveState(0);
                        //playCompMove();
                        if (playersInfo.getTurn() == playersInfo.getComp())
                            playersInfo.setMoveState(3);
                        else
                            playersInfo.setMoveState(4);
                        tDots.updateBoard();
                        setMoveState();
                        tDots.updateBoard();
                        //setMoveState();

                    }


                }

            }

        }

        return boardState;
    }

    //TODO:proveri brisanje fajla na kraju igre
    public void setMoveState() {
        if (playersInfo.getMoveState() == 0) {
            playersInfo.setMoveState(1);
            if (playersInfo.getTurn() == 0) {
                if (gameEnded(0)) {
                    playersInfo.setPlayerWon(0);


                    tDots.endGame();
                    return;

                }
            } else {
                if (gameEnded(1)) {
                    playersInfo.setPlayerWon(1);

                    tDots.endGame();
                    return;
                }
            }
            if (playersInfo.getTurn() == playersInfo.getComp()) {
                playCompMove();
            } else {

                checkForMoves();
                if (moves.isEmpty() && !playersInfo.sameDice()) {
                    playersInfo.setMoveState(4);
                    setMoveState();
                }
            }
        } else if (playersInfo.getMoveState() == 1) {
            playersInfo.setMoveState(2);
            spikesToShine.clear();


            if (!playersInfo.sameDice()) {
                playersInfo.changeTurn();
            }
            if (playersInfo.getTurn() == playersInfo.getComp()) {
                playersInfo.setMoveState(0);
                playersInfo.setCanRollDice(true);
                playCompMove();
            }


            playersInfo.setMoveState(0);
            playersInfo.setCanRollDice(true);
        } else if (playersInfo.getMoveState() == 3) {
            spikesToShine.clear();
            playersInfo.changeTurn();
            playersInfo.setCanRollDice(true);
            playersInfo.setMoveState(0);
            if (playersInfo.getTurn() == playersInfo.getComp()) playCompMove();

        } else if (playersInfo.getMoveState() == 4) {
            spikesToShine.clear();
            playersInfo.changeTurn();
            playersInfo.setCanRollDice(true);
            playersInfo.setMoveState(0);
            if (playersInfo.getTurn() == playersInfo.getComp()) playCompMove();
        }
        tDots.updateBoard();
    }


    public void playCompMove() {
        if (playersInfo.getMoveState() == 0) {
            shakeDice();
            tDots.refreshDice();
        }
        checkForMoves();
        Thread closeActivity = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tDots.drawDot(1);
                    Thread.sleep(400);
                    tDots.drawDot(2);
                    Thread.sleep(400);
                    tDots.drawDot(3);
                    Thread.sleep(400);
                    tDots.removeDots();
                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        closeActivity.run();
        if (!moves.isEmpty()) {
            Move move = null;
            if (playersInfo.getMoveState() == 0) {
                move = moves.get(0);
            } else if (playersInfo.getMoveState() == 1) {
                if (moves.size() > 1) {
                    move = moves.get(1);
                } else {
                    move = moves.get(0);
                }
            }
            int from = move.fromSpike;
            int to = move.toSpikes.get(0);

            grabChip(from - 1);

            dropChip(to - 1);

        } else {
            // dropChip(-3);
            playersInfo.setMoveState(3);
            setMoveState();
        }
        tDots.updateBoard();
    }

    private boolean gameEnded(int i) {
        if (i == 0) {
            return (boardState.spikeInfo.get(26).numOfChips == 15);
        } else return boardState.spikeInfo.get(27).numOfChips == 15;
    }

    public void shakeDice() {
        if (playersInfo.canRollDice()) {
            Random rand = new Random();
            int n = rand.nextInt(6) + 1;
            int m = rand.nextInt(6) + 1;

            playersInfo.setDiceOne(n);
            playersInfo.setDiceTwo(m);

            playersInfo.setDiceTwoPlayed(false);
            playersInfo.setDiceOnePlayed(false);

            playersInfo.setMoveState(0);

            checkForMoves();
            if (moves.isEmpty()) {
                if (playersInfo.getComp() == playersInfo.getTurn()) {
                    playersInfo.setMoveState(3);
                } else {
                    playersInfo.setMoveState(4);
                }

                setMoveState();
            }
//             else if (moves.isEmpty() && playersInfo.sameDice()) {
//                playersInfo.setCanRollDice(true);
//                playersInfo.setMoveState(0);
//
//            }
//
            else playersInfo.setCanRollDice(false);

        }
    }

    //calculate right dice
    //jail calc dice
    private void determentDice(int r) {
        if (playersInfo.getTurn() == 0) {
            if (boardState.getMovingChipFromSpike() == 24) {
                if (boardState.spikeInfo.get(r).getNum() - playersInfo.getDiceOne() == 0) {
                    playersInfo.setDiceOnePlayed(true);
                } else if (boardState.spikeInfo.get(r).getNum() - playersInfo.getDiceTwo() == 0) {
                    playersInfo.setDiceTwoPlayed(true);
                }
            }
            if (r == 26) {
                if (25 - playersInfo.getDiceOne() == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                    playersInfo.setDiceOnePlayed(true);
                } else if (25 - playersInfo.getDiceTwo() == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                    playersInfo.setDiceTwoPlayed(true);

                }
            } else if ((boardState.spikeInfo.get(r).getNum() - playersInfo.getDiceOne()) == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                playersInfo.setDiceOnePlayed(true);
            } else if ((boardState.spikeInfo.get(r).getNum() - playersInfo.getDiceTwo()) == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                playersInfo.setDiceTwoPlayed(true);
            }
        } else {

            if (boardState.getMovingChipFromSpike() == 25) {
                if (boardState.spikeInfo.get(r).getNum() + playersInfo.getDiceOne() == 25) {
                    playersInfo.setDiceOnePlayed(true);
                } else if (boardState.spikeInfo.get(r).getNum() + playersInfo.getDiceTwo() == 25) {
                    playersInfo.setDiceTwoPlayed(true);
                }
            }

            if (r == 27) {
                if (0 + playersInfo.getDiceOne() == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                    playersInfo.setDiceOnePlayed(true);
                } else if (0 + playersInfo.getDiceTwo() == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                    playersInfo.setDiceTwoPlayed(true);
                }
            } else if ((boardState.spikeInfo.get(r).getNum() + playersInfo.getDiceOne()) == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                playersInfo.setDiceOnePlayed(true);
            } else if ((boardState.spikeInfo.get(r).getNum() + playersInfo.getDiceTwo()) == boardState.spikeInfo.get(boardState.getMovingChipFromSpike()).getNum()) {
                playersInfo.setDiceTwoPlayed(true);
            }
        }
    }

    private boolean canDropOnSpike(int r) {
        int indexOfSpikeMovingFrom = boardState.movingChipFromSpike;
        int spikeInfo = (boardState.spikeInfo.get(indexOfSpikeMovingFrom)).num;
        Move move = getMoveBySpikeFrom(spikeInfo);
        if (move != null) {
            for (int i : move.toSpikes) {
                if (i == r) return true;
            }
        }
        return false;
    }


    public void checkForMoves() {
        spikesToShine.clear();
        moves.clear();
        int m1 = -1, m2 = -1;
        if (playersInfo.getTurn() == 0 && boardState.getBarInfo().get(0).numOfChips > 0) {
            if (!playersInfo.isDiceOnePlayed()) {
                m1 = calculateMoveFromBar(playersInfo.getDiceOne());
            }
            if (!playersInfo.isDiceTwoPlayed()) {
                m2 = calculateMoveFromBar(playersInfo.getDiceTwo());
            }

            if (m1 != -1 || m2 != -1) {
                Move move = new Move(25);
                move.setMustMoveFromBar(true);

                if (m1 != -1) {
                    move.addToSpikes(m1);
                }
                if (m2 != -1) {
                    move.addToSpikes(m2);
                }

                moves.add(move);
                spikesToShine.add(move.fromSpike);
            }

        } else if (playersInfo.getTurn() == 1 && boardState.getBarInfo().get(1).numOfChips > 0) {
            if (!playersInfo.isDiceOnePlayed()) {
                m1 = calculateMoveFromBar(playersInfo.getDiceOne());
            }
            if (!playersInfo.isDiceTwoPlayed()) {
                m2 = calculateMoveFromBar(playersInfo.getDiceTwo());
            }
            if (m1 != -1 || m2 != -1) {
                Move move = new Move(26);
                move.setMustMoveFromBar(true);

                if (m1 != -1) {
                    move.addToSpikes(m1);
                }
                if (m2 != -1) {
                    move.addToSpikes(m2);
                }

                moves.add(move);
                spikesToShine.add(move.fromSpike);
            }

        } else {

            for (SpikeInfo sp : boardState.spikeInfo) {
                if (sp.getNum() < 25) {
                    if (sp.getChipColor() == playersInfo.getTurn()) {
                        if (!playersInfo.isDiceOnePlayed()) {
                            m1 = diceMove(sp, playersInfo.getDiceOne());
                        }
                        if (!playersInfo.isDiceTwoPlayed()) {
                            m2 = diceMove(sp, playersInfo.getDiceTwo());
                        }
//               if (m1 != -1 || m2 != -1) {
//                   m3 = bothDiceMove(sp);
//               }
                        if (m1 != -1 || m2 != -1) {
                            Move move = new Move(sp.getNum());

                            if (m1 != -1) {
                                move.addToSpikes(m1);
                            }
                            if (m2 != -1) {
                                move.addToSpikes(m2);
                            }

                            moves.add(move);
                            spikesToShine.add(move.fromSpike);
                        }
                    }
                }

            }
        }
        //????????????????????????

    }

    private boolean canMoveFromBoard(int i) {
        int chipsOutOfHome = 0;
        if (i == 0) {
            for (SpikeInfo si : boardState.spikeInfo) {
                if (si.getNum() <= 18 && si.getNum() >= 1 && si.getChipColor() == 0) {
                    chipsOutOfHome += si.numOfChips;
                }
            }
        } else {
            for (SpikeInfo si : boardState.spikeInfo) {
                if (si.getNum() >= 7 && si.getNum() <= 24 && si.getChipColor() == 1) {
                    chipsOutOfHome += si.numOfChips;
                }
            }
        }
        return chipsOutOfHome == 0;
    }


    private int calculateMoveFromBar(int diceValue) {
        int spikeIndex;
        if (playersInfo.getTurn() == 0) {
            spikeIndex = 0 + diceValue;
        } else {
            spikeIndex = 25 - diceValue;
        }
        if (spikeIndex > 24 || spikeIndex < 1) return -1;
        SpikeInfo spikeToCheck = boardState.spikeInfo.get(spikeIndex - 1);
        if (isFree(spikeToCheck) || isSameColor(spikeToCheck) || canOvertake(spikeToCheck)) {
            return spikeIndex;
        } else return -1;
    }

    private int bothDiceMove(SpikeInfo spikeInfo) {
        int spikeIndex;
        if (playersInfo.getTurn() == 0) {
            spikeIndex = spikeInfo.getNum() + playersInfo.getDiceOne() + playersInfo.getDiceTwo();
        } else {
            spikeIndex = spikeInfo.getNum() - playersInfo.getDiceOne() - playersInfo.getDiceTwo();
        }
        if (spikeIndex > 24) return -1;
        SpikeInfo spikeToCheck = boardState.spikeInfo.get(spikeIndex - 1);

        if (isFree(spikeToCheck) || isSameColor(spikeToCheck) || canOvertake(spikeToCheck)) {
            return spikeIndex;
        } else return -1;
    }

    private int diceMove(SpikeInfo spikeInfo, int diceValue) {
        int spikeIndex;
        if (playersInfo.getTurn() == 0) {
            spikeIndex = spikeInfo.getNum() + diceValue;
        } else {
            spikeIndex = spikeInfo.getNum() - diceValue;
        }
        if ((spikeIndex > 24 && playersInfo.getTurn() == 0 && canMoveFromBoard(playersInfo.getTurn()))) {
            if (spikeIndex == 25) return 27;
            else return -1;
        } else if ((spikeIndex < 1 && playersInfo.getTurn() == 1 && canMoveFromBoard(playersInfo.getTurn()))) {
            if (spikeIndex == 0) return 28;
            else return -1;
        }
        if (spikeIndex > 24 || spikeIndex < 1) return -1;
        SpikeInfo spikeToCheck = boardState.spikeInfo.get(spikeIndex - 1);
        if (isFree(spikeToCheck) || isSameColor(spikeToCheck) || canOvertake(spikeToCheck)) {
            return spikeIndex;
        } else return -1;
    }

    public boolean isFree(SpikeInfo sp) {
        return (sp.getNumOfChips() == 0);

    }

    public boolean isSameColor(SpikeInfo sp) {
        return (sp.getChipColor() == playersInfo.getTurn());
    }

    public boolean canOvertake(SpikeInfo sp) {
        return (sp.getChipColor() != playersInfo.getTurn() && sp.getNumOfChips() == 1);
    }

    public Move getMoveBySpikeFrom(int index) {
        for (Move m : moves) {
            if (m.fromSpike == index) return m;
        }
        return null;
    }
}
