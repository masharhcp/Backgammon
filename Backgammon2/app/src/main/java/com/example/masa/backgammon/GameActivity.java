package com.example.masa.backgammon;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.masa.backgammon.database.DbHelper;
import com.example.masa.backgammon.database.TableEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, ThinkingDots {

    ArrayList<Rect> spikes;
    ArrayList<Rect> bars;
    ArrayList<SpikeInfo> spikeInfo;
    ArrayList<BarInfo> barInfo;
    GameModel gameModel;
    boolean saved = false;
    onDropBck onDropBack = null;
    DbHelper helper;
    private SensorManager sensorManager;
    private ShakeSensor shakeSensor;
    private MediaPlayer mediaPlayer;
    private boolean shakeWasStarted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        helper = new DbHelper(this);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        extras.getBundle("");
        boolean continueGame = extras.getBoolean("ContinueGame");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_playgame);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        spikes = new ArrayList<>();
        bars = new ArrayList<>();
        spikeInfo = new ArrayList<>();
        barInfo = new ArrayList<>();
        MyImageView viewById = findViewById(R.id.myImageView);
        viewById.setOnTouchListener(this);

        if (!continueGame) {
            //restoreGameModel
            gameModel = new GameModel();
            gameModel.setDots(this);
            String whitePlayerName = extras.getString("WhitePlayerName");
            String blackPlayerName = extras.getString("BlackPlayerName");
            int comp = extras.getInt("ComputerPlayer", 2);
            assembleTheBoard();
            PlayersInfo playersInfo = new PlayersInfo(whitePlayerName, blackPlayerName, comp);
            gameModel.setPlayersInfo(playersInfo);

            viewById.setBlackName(blackPlayerName);
            viewById.setWhiteName(whitePlayerName);

            viewById.setDiceOne(playersInfo.getDiceOne());
            viewById.setDiceTwo(playersInfo.getDiceTwo());

        } else {
            restoreGameModel();
        }
        setUpShakeDetector();
    }

    private void setUpShakeDetector() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeSensor = new ShakeSensor();
        setShakeSensor();
        shakeSensor.setShakeListener(new ShakeListener() {
            public void onShakeStarted() {
                if (gameModel.getPlayersInfo().canRollDice()) {
                    System.out.println("Shake started");
                    shakeWasStarted = true;
                    mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.rollingdice);
                    if (mediaPlayer != null && mediaPlayer.isPlaying())
                        return;
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            }

            @Override
            public void onShakeFinished() {
                if (shakeWasStarted){
                System.out.println("Shake stopped");
                mediaPlayer.stop();
                shakeDice();
                shakeWasStarted = false;

                }

            }
        });
    }


    private void restoreGameModel() {
        FileInputStream fis = null;
        try {
            fis = this.openFileInput("Saved_Game");
            ObjectInputStream is = new ObjectInputStream(fis);
            gameModel = (GameModel) is.readObject();
            is.close();
            fis.close();
            MyImageView viewById = (MyImageView) findViewById(R.id.myImageView);
            viewById.setTurn(gameModel.getPlayersInfo().getTurn());
            viewById.setSpikesToShine(gameModel.getSpikesToShine());
            viewById.setBlackName(gameModel.getPlayersInfo().getBlackPlayerName());
            viewById.setWhiteName(gameModel.getPlayersInfo().getWhitePlayerName());
            viewById.setDiceOne(gameModel.getPlayersInfo().getDiceOne());
            viewById.setDiceTwo(gameModel.getPlayersInfo().getDiceTwo());
            viewById.setBoardState(gameModel.getBoardState());
            createRectangles();
            //putChips();
            viewById.setSpikes(spikes);
            viewById.setBars(bars);
            spikeInfo = gameModel.getBoardState().spikeInfo;
            barInfo = gameModel.getBoardState().barInfo;
            gameModel.setDots(this);
            viewById.invalidate();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void assembleTheBoard() {
        createRectangles();
        putChips();
    }

    public void setShakeSensor() {
        sensorManager.registerListener(shakeSensor, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
    }




    //maps the board with rectangles used for moving chips
    //saves the rectangles in a list for further comparing
    private void createRectangles() {
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        p.setAlpha(50);

        //upperLeft six

        Rect r13 = new Rect(200, 38, 310, 494);
        Rect r14 = new Rect(313, 38, 423, 494);
        Rect r15 = new Rect(426, 38, 536, 494);
        Rect r16 = new Rect(539, 38, 649, 494);
        Rect r17 = new Rect(652, 38, 762, 494);
        Rect r18 = new Rect(765, 38, 875, 494);

        //upperRight six
        Rect r19 = new Rect(1039, 38, 1149, 494);
        Rect r20 = new Rect(1152, 38, 1262, 494);
        Rect r21 = new Rect(1265, 38, 1375, 494);
        Rect r22 = new Rect(1378, 38, 1488, 494);
        Rect r23 = new Rect(1491, 38, 1601, 494);
        Rect r24 = new Rect(1604, 38, 1714, 494);


        //bottomLeft six
        Rect r12 = new Rect(200, 581, 310, 1040);
        Rect r11 = new Rect(313, 581, 423, 1040);
        Rect r10 = new Rect(426, 581, 536, 1040);
        Rect r9 = new Rect(539, 581, 649, 1040);
        Rect r8 = new Rect(652, 581, 762, 1040);
        Rect r7 = new Rect(765, 581, 875, 1040);

        //bottomRight six
        Rect r6 = new Rect(1039, 581, 1149, 1040);
        Rect r5 = new Rect(1152, 581, 1262, 1040);
        Rect r4 = new Rect(1265, 581, 1375, 1040);
        Rect r3 = new Rect(1378, 581, 1488, 1040);
        Rect r2 = new Rect(1491, 581, 1601, 1040);
        Rect r1 = new Rect(1604, 581, 1714, 1040);


        spikes.add(r1);
        spikes.add(r2);
        spikes.add(r3);
        spikes.add(r4);
        spikes.add(r5);
        spikes.add(r6);

        spikes.add(r7);
        spikes.add(r8);
        spikes.add(r9);
        spikes.add(r10);
        spikes.add(r11);
        spikes.add(r12);

        spikes.add(r13);
        spikes.add(r14);
        spikes.add(r15);
        spikes.add(r16);
        spikes.add(r17);
        spikes.add(r18);

        spikes.add(r19);
        spikes.add(r20);
        spikes.add(r21);
        spikes.add(r22);
        spikes.add(r23);
        spikes.add(r24);
        Rect upperMiddle = new Rect(890, 38, 1015, 494);
        Rect bottomMiddle = new Rect(890, 581, 1015, 1040);
        Rect whiteHomeSpike = new Rect(1770, 38, 1880, 494);
        Rect blackHomeSpike = new Rect(1770, 581, 1880, 1040);
        ;
        bars.add(bottomMiddle);
        bars.add(upperMiddle);
        bars.add(whiteHomeSpike);
        bars.add(blackHomeSpike);
        spikes.add(bottomMiddle);
        spikes.add(upperMiddle);
        spikes.add(whiteHomeSpike);
        spikes.add(blackHomeSpike);
        MyImageView viewById = (MyImageView) findViewById(R.id.myImageView);
        viewById.setSpikes(spikes);
        viewById.setBars(bars);


    }

    //0b 5c 7c 11b 12c 16b 18b 23c
    //puts chips in their starting position
    //or in the current position if the game is being continued
    private void putChips() {
        MyImageView viewById = (MyImageView) findViewById(R.id.myImageView);


        for (int i = 0; i < 28; i++) {
            SpikeInfo si = new SpikeInfo();
            if (i == 0) {
                si.setChipColor(0);
                si.setNumOfChips(2);
                si.setNum(1);
            } else if (i == 5) {
                si.setChipColor(1);
                si.setNumOfChips(5);
                si.setNum(6);
            } else if (i == 7) {
                si.setChipColor(1);
                si.setNumOfChips(3);
                si.setNum(8);
            } else if (i == 11) {
                si.setChipColor(0);
                si.setNumOfChips(5);
                si.setNum(12);
            } else if (i == 12) {
                si.setChipColor(1);
                si.setNumOfChips(5);
                si.setNum(13);
            } else if (i == 16) {
                si.setChipColor(0);
                si.setNumOfChips(3);
                si.setNum(17);
            } else if (i == 18) {
                si.setChipColor(0);
                si.setNumOfChips(5);
                si.setNum(19);
            } else if (i == 23) {
                si.setChipColor(1);
                si.setNumOfChips(2);
                si.setNum(24);

            } else if (i == 24) {
                si.setChipColor(0);
                si.setNumOfChips(0);
                si.setNum(25);
            } else if (i == 25) {
                si.setChipColor(1);
                si.setNumOfChips(0);
                si.setNum(26);
            }
            //whiteBar 26
            else if (i == 26) {
                si.setChipColor(0);
                si.setNumOfChips(0);
                si.setNum(27);
            }
            //blackBar 27
            else if (i == 27) {
                si.setChipColor(1);
                si.setNumOfChips(0);
                si.setNum(28);
            } else {
                si.setChipColor(-1);
                si.setNumOfChips(0);
                si.setNum(i + 1);
            }


            spikeInfo.add(si);
        }

//        for (int i = 0; i < 28; i++) {
//            SpikeInfo si = new SpikeInfo();
//            if (i == 0) {
//                si.setChipColor(1);
//                si.setNumOfChips(1);
//                si.setNum(1);
//            } else if (i == 5) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(6);
//            } else if (i == 7) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(8);
//            } else if (i == 11) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(12);
//            } else if (i == 12) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(13);
//            } else if (i == 16) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(17);
//            } else if (i == 18) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(19);
//            } else if (i == 23) {
//                si.setChipColor(0);
//                si.setNumOfChips(1);
//                si.setNum(24);
//
//            } else if (i == 24) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(25);
//            } else if (i == 25) {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(26);
//            }
//            //whiteBar 26
//            else if (i == 26) {
//                si.setChipColor(0);
//                si.setNumOfChips(14);
//                si.setNum(27);
//            }
//            //blackBar 27
//            else if (i == 27) {
//                si.setChipColor(1);
//                si.setNumOfChips(14);
//                si.setNum(28);
//            } else {
//                si.setChipColor(-1);
//                si.setNumOfChips(0);
//                si.setNum(i + 1);
//            }
//
//
//            spikeInfo.add(si);
//        }

        BarInfo whiteBar = new BarInfo(0, 0);
        BarInfo blackBar = new BarInfo(1, 0);
        BarInfo whiteHomeBar = new BarInfo(0, 0);
        BarInfo blackHomeBar = new BarInfo(1, 0);
        BoardState bs = new BoardState();
        bs.setSpikeInfo(spikeInfo);
        bs.setBarInfo(barInfo);
        barInfo.add(whiteBar);
        barInfo.add(blackBar);

        barInfo.add(whiteHomeBar);
        barInfo.add(blackHomeBar);
        viewById.setBoardState(bs);
        gameModel.setBoardState(bs);

    }

    private class onDropBck extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            if (gameModel.getDots() != null) {
                gameModel.dropChip(integers[0]);
                return null;
            }
            return null;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float coords[];

        float x = event.getX();
        float y = event.getY();

        // Trenutak kada se dogodio dogadjaj (ms) relativno u odnosu na trenutak pokretanja sistema
        // (vrijeme "dubokog sna" nije uracunato).
        long eventTime = event.getEventTime();
        MyImageView viewById = findViewById(R.id.myImageView);
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                if (gameModel.getPlayersInfo().getTurn() != gameModel.getPlayersInfo().getComp()) {
                    grabChip((int) x, (int) y);
                    viewById.invalidate();
                }
                return true;

            case MotionEvent.ACTION_UP:
                dropChip((int) x, (int) y);
                viewById.invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:

                dragChip((int) x, (int) y);
                viewById.invalidate();
                return true;
            default:

                return false;
        }
    }


    public void grabChip(int x, int y) {
        MyImageView myImageView = findViewById(R.id.myImageView);
        int spikeIndex;
        spikeIndex = myImageView.grabChip(x, y);
        BoardState boardState = gameModel.grabChip(spikeIndex);
        myImageView.setBoardState(boardState);
        myImageView.setSpikesToShine(gameModel.getSpikesToShine());

    }

    public void dropChip(int x, int y) {
        gameModel.setDots(this);
        MyImageView myImageView = findViewById(R.id.myImageView);
        int spikeIndex;
        spikeIndex = myImageView.dropChip(x, y);
        onDropBack = (onDropBck) new onDropBck().execute(spikeIndex);

        myImageView.setTurn(gameModel.getPlayersInfo().getTurn());


    }

    public void dragChip(int x, int y) {
        MyImageView myImageView = findViewById(R.id.myImageView);
        myImageView.dragChip(x, y);
    }


    public long insertScore() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean inversePlayers = checkForInversePlayers();
        if (inversePlayers) {
            values.put(TableEntry.PLAYER_ONE, gameModel.getPlayersInfo().getBlackPlayerName());
            values.put(TableEntry.PLAYER_TWO, gameModel.getPlayersInfo().getWhitePlayerName());
            if (gameModel.getPlayersInfo().getPlayerWon() == 0) {
                values.put(TableEntry.PLAYER_ONE_POINTS, 0);
                values.put(TableEntry.PLAYER_TWO_POINTS, 1);
            } else {
                values.put(TableEntry.PLAYER_ONE_POINTS, 1);
                values.put(TableEntry.PLAYER_TWO_POINTS, 0);
            }

        } else {
            values.put(TableEntry.PLAYER_ONE, gameModel.getPlayersInfo().getWhitePlayerName());
            values.put(TableEntry.PLAYER_TWO, gameModel.getPlayersInfo().getBlackPlayerName());
            if (gameModel.getPlayersInfo().getPlayerWon() == 0) {
                values.put(TableEntry.PLAYER_ONE_POINTS, 1);
                values.put(TableEntry.PLAYER_TWO_POINTS, 0);
            } else {
                values.put(TableEntry.PLAYER_ONE_POINTS, 0);
                values.put(TableEntry.PLAYER_TWO_POINTS, 1);
            }
        }


        long currentTime = System.currentTimeMillis();
        String dateString = (new SimpleDateFormat("dd/MM/yyyy hh:mm")).format(new Date(currentTime));
        values.put(TableEntry.TIME, dateString);
        db.insert(TableEntry.TABLE_NAME, null, values);
        return currentTime;
    }

    private boolean checkForInversePlayers() {
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        String query = "select " + TableEntry.PLAYER_ONE + " , " + TableEntry.PLAYER_TWO + " from " + TableEntry.TABLE_NAME + " where " + TableEntry.PLAYER_ONE + " = '" + gameModel.getPlayersInfo().getBlackPlayerName() + "' and " + TableEntry.PLAYER_TWO + "= '" + gameModel.getPlayersInfo().getWhitePlayerName() + "'";
        Cursor cursor = readableDatabase.rawQuery(query, null);

        return (cursor.getCount() > 0);
    }


    //ubacivanje scora u bazu i pozivanje aktivnosti koj apokazuje rezultat te igre i svih rezultata izmedju to dvoje igraca
    @Override
    public void endGame() {
        long time = insertScore();


        Thread closeActivity = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(1000);

                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        closeActivity.run();
        Intent intent = new Intent(this, GameDetailsActivity.class);

        Bundle bundle = new Bundle();
        if (checkForInversePlayers()) {
            bundle.putString("WhitePlayerName", gameModel.getPlayersInfo().getBlackPlayerName());
            bundle.putString("BlackPlayerName", gameModel.getPlayersInfo().getWhitePlayerName());
        } else {
            bundle.putString("WhitePlayerName", gameModel.getPlayersInfo().getWhitePlayerName());
            bundle.putString("BlackPlayerName", gameModel.getPlayersInfo().getBlackPlayerName());
        }
        bundle.putLong("Time", time);

        if (gameModel.getPlayersInfo().getPlayerWon() == 0) {
            bundle.putInt("WhitePlayerPoints", 1);
            bundle.putInt("BlackPlayerPoints", 0);
        } else {
            bundle.putInt("WhitePlayerPoints", 0);
            bundle.putInt("BlackPlayerPoints", 1);
        }
        File dir = getFilesDir();
        File file = new File(dir, "Saved_Game");
        String path = file.getPath();
        System.out.println("PUTANJA JE " + path);
        boolean deleted = file.delete();
        System.out.println(deleted);
        if (file.exists()) {
            file.delete();
        }
        gameModel = null;

        bundle.putBoolean("intentFromScores", false);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    //shake sensore instead
    public void shakeDice(View view) {

        if (gameModel.getPlayersInfo().canRollDice()) {
            gameModel.shakeDice();
            MyImageView viewById = findViewById(R.id.myImageView);
            viewById.diceOne = gameModel.getPlayersInfo().getDiceOne();
            viewById.diceTwo = gameModel.getPlayersInfo().getDiceTwo();
            gameModel.checkForMoves();
            viewById.setSpikesToShine(gameModel.getSpikesToShine());
            viewById.invalidate();
        }

    }

    public void shakeDice() {

        if (gameModel.getPlayersInfo().canRollDice()) {
            gameModel.shakeDice();
            MyImageView viewById = findViewById(R.id.myImageView);
            viewById.diceOne = gameModel.getPlayersInfo().getDiceOne();
            viewById.diceTwo = gameModel.getPlayersInfo().getDiceTwo();
            gameModel.checkForMoves();
            viewById.setSpikesToShine(gameModel.getSpikesToShine());
            viewById.invalidate();
        }

    }

    @Override
    public void refreshDice() {
        MyImageView myImageView = (MyImageView) findViewById(R.id.myImageView);
        myImageView.diceOne = gameModel.getPlayersInfo().getDiceOne();
        myImageView.diceTwo = gameModel.getPlayersInfo().getDiceTwo();
        gameModel.checkForMoves();
        myImageView.setSpikesToShine(gameModel.getSpikesToShine());

        myImageView.invalidate();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Thread closeActivity = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(1000);

                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        closeActivity.run();
        FileOutputStream fos = null;
        if (gameModel != null) {
            try {
                fos = this.openFileOutput("Saved_Game", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ObjectOutputStream os = null;
            try {
                os = new ObjectOutputStream(fos);
                gameModel.setDots(null);
                os.writeObject(gameModel);
                os.close();
                fos.close();
                saved = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //onDropBck.cancel(true);

    }

    @Override
    public void onBackPressed() {
        Thread closeActivity = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(1500);

                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        closeActivity.run();
        FileOutputStream fos = null;
        if (gameModel != null) {
            try {
                fos = this.openFileOutput("Saved_Game", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ObjectOutputStream os = null;
            try {
                os = new ObjectOutputStream(fos);
                gameModel.setDots(null);
                os.writeObject(gameModel);
                os.close();
                fos.close();
                saved = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //onDropBck.cancel(true);
        finish();


    }

    @Override
    public void drawDot(int i) {
        MyImageView viewById = findViewById(R.id.myImageView);
        if (i == 1) {
            viewById.addDot(new Rect(830, 650, 880, 700));
            viewById.invalidate();
        } else if (i == 2) {
            viewById.addDot(new Rect(930, 650, 980, 700));
            viewById.invalidate();
        } else {
            viewById.addDot(new Rect(1030, 650, 1080, 700));
            viewById.invalidate();
        }
    }

    @Override
    public void removeDots() {
        MyImageView viewById = findViewById(R.id.myImageView);
        viewById.removeDots();
    }

    @Override
    public void updateBoard() {
        final MyImageView myImageView = findViewById(R.id.myImageView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                myImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        myImageView.setBoardState(gameModel.getBoardState());
                        myImageView.setSpikesToShine(gameModel.getSpikesToShine());
                        myImageView.setTurn(gameModel.getPlayersInfo().getTurn());
                        myImageView.invalidate();
                    }
                });
            }
        }).start();
        //myImageView.invalidate();
    }

}
