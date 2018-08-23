package com.example.masa.backgammon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyImageView extends android.support.v7.widget.AppCompatImageView {

    ArrayList<Rect> spikes = new ArrayList<>();
    ArrayList<Rect> bars = new ArrayList<>();
    ArrayList<Rect> dots = new ArrayList<>();
    ArrayList<Integer> spikesToShine = new ArrayList<>();
    int turn;
    BoardState boardState;
    String whiteName, blackName;
    int diceOne, diceTwo;



    public void addDot(Rect r){
        dots.add(r);
        this.invalidate();
    }

    public void removeDots(){
        dots.clear();
        this.invalidate();

    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public ArrayList<Integer> getSpikesToShine() {
        return spikesToShine;
    }

    public void setSpikesToShine(ArrayList<Integer> spikesToShine) {
        this.spikesToShine = spikesToShine;
    }

    public int getDiceOne() {
        return diceOne;
    }

    public void setDiceOne(int diceOne) {
        this.diceOne = diceOne;
    }

    public int getDiceTwo() {
        return diceTwo;
    }

    public void setDiceTwo(int diceTwo) {
        this.diceTwo = diceTwo;
    }

    public ArrayList<Rect> getBars() {
        return bars;
    }

    public void setBars(ArrayList<Rect> bars) {
        this.bars = bars;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    public int getMovingChipColor() {
        return boardState.movingChipColor;
    }

    public void setMovingChipColor(int movingChipColor) {
        this.boardState.movingChipColor = movingChipColor;
    }

    public int getChipX() {
        return boardState.chipX;
    }

    public void setChipX(int chipX) {
        this.boardState.chipX = chipX;
    }

    public int getChipY() {
        return boardState.chipY;
    }

    public void setChipY(int chipY) {
        this.boardState.chipY = chipY;
    }

    public boolean isMovingChip() {
        return boardState.movingChip;
    }

    public void setMovingChip(boolean movingChip) {
        this.boardState.movingChip = movingChip;
    }

    public ArrayList<Rect> getSpikes() {
        return spikes;
    }

    public void setSpikes(ArrayList<Rect> spikes) {
        this.spikes = spikes;
    }

    public ArrayList<SpikeInfo> getSpikeInfo() {
        return boardState.spikeInfo;
    }

    public void setSpikeInfo(ArrayList<SpikeInfo> spikeInfo) {
        this.boardState.spikeInfo = spikeInfo;
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //drawNames method

        drawChips(canvas);
        drawDice(canvas);
        shineSpikes(canvas);
        drawNames(canvas);
        drawDots(canvas);
    }

    private void drawDots(Canvas canvas) {
        for (Rect r: dots){
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            canvas.drawRect(r, paint);
        }
    }

    private void drawNames(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(26);
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(26);
        if (turn == 0){
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
            paint.setColor(Color.GREEN);
        }
        else {
            paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint2.setFlags(Paint.UNDERLINE_TEXT_FLAG);
            paint2.setColor(Color.GREEN);
        }



        canvas.drawText(whiteName, 1770, 25, paint);
        canvas.drawText(blackName, 1770, 1069, paint2);
    }

    private void shineSpikes(Canvas canvas) {
        for (int i: spikesToShine){
            Rect rect = spikes.get(i - 1);
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setAlpha(50);
            canvas.drawRect(rect, paint);
        }
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAlpha(50);
//        canvas.drawRect(bars.get(2),paint);
//        canvas.drawRect(bars.get(3),paint);
    }

    private void drawDice(Canvas canvas) {
        drawDiceInfo(diceOne, 1, canvas);
        drawDiceInfo(diceTwo, 2, canvas);

    }

    private void drawDiceInfo(int diceValue, int pos, Canvas canvas) {
        Rect position;
        Bitmap dice;

        if (pos == 1){
            position = new Rect( canvas.getWidth()/2-55,canvas.getHeight()/2-55-50,canvas.getWidth()/2+45,canvas.getHeight()/2+45-50);

        }
        else {
            position = new Rect( canvas.getWidth()/2-55,canvas.getHeight()/2-55+50,canvas.getWidth()/2+45,canvas.getHeight()/2+45+50);

        }

        switch(diceValue){
            case 1:
                dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice1);
                break;
            case 2:
                dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice2);
                break;
            case 3:
                dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice3);
                break;
            case 4:
                dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice4);
                break;
            case 5:
                dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice5);
                break;
            case 6:
                dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice6);
                break;
                default:
                    dice = BitmapFactory.decodeResource(getResources(), R.drawable.dice1);
        }

        canvas.drawBitmap(dice,null,  position, null);

    }


    public void drawChips(Canvas canvas){
        BarInfo whiteBarInfo;
        BarInfo blackBarInfo;
        Bitmap blackChipOrig = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        Bitmap whitekChipOrig = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        Bitmap whiteChip = Bitmap.createScaledBitmap(whitekChipOrig, 120, 120, true);
        Bitmap blackChip = Bitmap.createScaledBitmap(blackChipOrig, 120, 120, true);
        if (boardState.movingChip){
            if (boardState.movingChipColor==0){
                canvas.drawBitmap(whiteChip, boardState.chipX - whiteChip.getWidth()/2, boardState.chipY - whiteChip.getHeight()/2, null );
            }
            else {
                // canvas.drawBitmap(blackChip, null, new Rect(chipX - whitekChip.getWidth()/2, chipY - whitekChip.getHeight()/2,  ) ,null);
                canvas.drawBitmap(blackChip,boardState.chipX - blackChip.getWidth()/2, boardState.chipY - blackChip.getHeight()/2, null );
            }
        }
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        p.setAlpha(50);

        for (SpikeInfo spike:boardState.spikeInfo){
            Rect rect = spikes.get(boardState.spikeInfo.indexOf(spike));
            if(spike.getNumOfChips()!=0){
                if (spike.getNumOfChips()<=5){
                    if (spike.getNum()<=12){

                        for (int i =0; i<spike.getNumOfChips(); i++) {
                            if (spike.getChipColor()==0)
                                canvas.drawBitmap(whiteChip, null, new Rect(rect.left + 10,rect.bottom - 90 * (i+1), rect.right - 10, rect.bottom - 90 * i ), null);
                            else
                                canvas.drawBitmap(blackChip, null, new Rect(rect.left + 10, rect.bottom - 90 * (i+1), rect.right - 10,  rect.bottom - 90 * i), null);
                        }
                    }
                    else {

                        for (int i =0; i<spike.getNumOfChips(); i++) {
                            if (spike.getChipColor()==0)
                                canvas.drawBitmap(whiteChip, null, new Rect(rect.left + 10, rect.top + 90 * i, rect.right - 10,  rect.top + 90 *(i+1)), null);
                            else
                                canvas.drawBitmap(blackChip, null, new Rect(rect.left + 10,rect.top + 90 * i, rect.right - 10,  rect.top + 90 * (i+1)), null);
                        }
                    }
                }
                else {
                    if (spike.getNum()<=12){

                        for (int i =0; i<spike.getNumOfChips(); i++) {
                            if (spike.getChipColor()==0)
                                canvas.drawBitmap(whiteChip, null, new Rect(rect.left + 10,rect.bottom - 90 * (i+1)+25*(spike.getNumOfChips()/4)*i, rect.right - 10, rect.bottom - 90 * i+25*(spike.getNumOfChips()/4)*i ), null);
                            else
                                canvas.drawBitmap(blackChip, null, new Rect(rect.left + 10, rect.bottom - 90 * (i+1)+25*(spike.getNumOfChips()/4)*i, rect.right - 10,  rect.bottom - 90 * i+25*(spike.getNumOfChips()/4)*i), null);
                        }
                    }
                    else {

                        for (int i =0; i<spike.getNumOfChips(); i++) {
                            if (spike.getChipColor()==0)
                                canvas.drawBitmap(whiteChip, null, new Rect(rect.left + 10, rect.top + 90 * i-25*(spike.getNumOfChips()/4)*(i), rect.right - 10,  rect.top + 90 * (i+1)-25*(spike.getNumOfChips()/4)*i), null);
                            else
                                canvas.drawBitmap(blackChip, null, new Rect(rect.left + 10,rect.top + 90 * i-25*(spike.getNumOfChips()/4)*(i), rect.right - 10,  rect.top + 90 * (i+1)-25*(spike.getNumOfChips()/4)*i), null);
                        }
                    }
                }
            }
        }


        ArrayList<BarInfo> barInfo = boardState.getBarInfo();
        whiteBarInfo = barInfo.get(0);
        blackBarInfo = barInfo.get(1);
        if (whiteBarInfo.numOfChips>0){
            canvas.drawBitmap(whiteChip, null, new Rect(bars.get(0).left+25, bars.get(0).bottom-90, bars.get(0).left+90+25, bars.get(0).bottom )  ,null);
        }

        if (blackBarInfo.numOfChips>0){
            canvas.drawBitmap(blackChip, null, new Rect(bars.get(1).left+25, bars.get(1).top, bars.get(1).left+90+25, bars.get(1).top+90 )  ,null);
        }
    }

    public int grabChip(int x, int y) {
        for (Rect r : spikes) {
            if (r.contains((int) x, (int) y)) {
                return spikes.indexOf(r);
            }


        }
        return -1;
    }
    public int dropChip(int x, int y) {
        boolean spikeFound = false;
        for (Rect r : spikes) {
            if (r.contains((int) x, (int) y)) {
                return spikes.indexOf(r);
            }

        }
        return -1;
    }

    public void dragChip(int x, int y) {
        if (boardState.movingChipFromSpike != -1) {
            Rect rect = spikes.get(boardState.movingChipFromSpike);
            SpikeInfo spikeInfo = this.boardState.spikeInfo.get(boardState.movingChipFromSpike);

            this.setChipX((int) x);
            this.setChipY((int) y);
        }
    }

}
