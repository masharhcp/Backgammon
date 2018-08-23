package com.example.masa.backgammon;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.masa.backgammon.database.DbHelper;
import com.example.masa.backgammon.database.TableEntry;

import java.sql.Date;
import java.text.SimpleDateFormat;



public class GameDetailsActivity extends AppCompatActivity {

    private DbHelper helper;
    int whitePlayerTotal;
    int blackPlayerTotal;
    String whitePlayerName;
    String blackPlayerName;
    boolean fromScores;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        helper = new DbHelper(this);
        updateScores();
    }

    private void updateScores() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        extras.getBundle("");
        whitePlayerName= extras.getString("WhitePlayerName");
        blackPlayerName = extras.getString("BlackPlayerName");
        fromScores= extras.getBoolean("intentFromScores");
        if (!extras.getBoolean("intentFromScores")){
        long time = extras.getLong("Time");
        int whitePlayerPoints =  extras.getInt("WhitePlayerPoints");
        int blackPlayerPoints = extras.getInt("BlackPlayerPoints");
            TextView whitePlayerPointsView = (TextView)findViewById(R.id.whitePlayerGameResult);
            TextView blackPlayerPointsView = (TextView)findViewById(R.id.blackPlayerGameResult);
            whitePlayerPointsView.setText(""+whitePlayerPoints);
            blackPlayerPointsView.setText(""+blackPlayerPoints);
            String dateString = (new SimpleDateFormat("dd/MM/yyyy hh:mm")).format(new Date(time));
            TextView dateView = (TextView)findViewById(R.id.dateTime);
            dateView.setText(dateString);
        }
        getAllGames(whitePlayerName, blackPlayerName);
        TextView whitePlayerNameView = (TextView)findViewById(R.id.whitePlayerName);
        TextView blackPlayerNameView = (TextView)findViewById(R.id.blackPlayerName);


        TextView totalWhiteView = (TextView)findViewById(R.id.totalWhite);
        TextView totalBlackView = (TextView)findViewById(R.id.totalBlack);
        whitePlayerNameView.setText(whitePlayerName);
        blackPlayerNameView.setText(blackPlayerName);


        totalWhiteView.setText(""+whitePlayerTotal);
        totalBlackView.setText(""+blackPlayerTotal);
    }


    //dohvati sve igre izmedju tih igraca i saberi ispod
    public void getAllGames(String playerOne, String playerTwo){

        SQLiteDatabase db = helper.getReadableDatabase();

        //player one player two redosled u ubacivanju

        String selection = TableEntry.PLAYER_ONE + "= ? AND " + TableEntry.PLAYER_TWO + " = ?";
        String selectionArgs[] = {playerOne, playerTwo};
        Cursor cursor = db.query(TableEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        ListView scoresListView = findViewById(R.id.resultList);

        String[] from = {TableEntry.PLAYER_ONE_POINTS, TableEntry.TIME, TableEntry.PLAYER_TWO_POINTS};
        int[] to = {R.id.whitePlayerScore, R.id.dateTime, R.id.blackPlayerScore};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.single_list_item_details, cursor, from, to, 0);
        scoresListView.setAdapter(sca);

        cursor.moveToFirst();
        cursor.moveToPrevious();

        whitePlayerTotal =  calculateWins(TableEntry.PLAYER_ONE_POINTS, cursor);
        blackPlayerTotal =  calculateWins(TableEntry.PLAYER_TWO_POINTS, cursor);
        //helper.close();


    }

    private int calculateWins(String playerTwoPoints, Cursor cursor) {
        int playerPoints = 0;
        cursor.moveToFirst();
        cursor.moveToPrevious();
                while (cursor.moveToNext()){
                    playerPoints += cursor.getInt(cursor.getColumnIndex(playerTwoPoints));
                }
        return playerPoints;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         if (fromScores){
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);}
        else {
             Intent intent = new Intent(this, MainActivity.class);
             startActivity(intent);
         }
        finish();
    }


    public void onClickDelete(View v){
        SQLiteDatabase db = helper.getWritableDatabase();
        String query ="delete from "+ TableEntry.TABLE_NAME +" where "+ TableEntry.PLAYER_ONE +"= '"+whitePlayerName+"' and "+ TableEntry.PLAYER_TWO +" = '"+blackPlayerName+"'";
        db.execSQL(query);
        db.close();
        updateScores();
    }


}
