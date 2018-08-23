package com.example.masa.backgammon;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.masa.backgammon.database.DbHelper;
import com.example.masa.backgammon.database.TableEntry;

public class ScoreActivity extends AppCompatActivity {

    private DbHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        helper = new DbHelper(this);
        updateScores();

    }


    public void updateScores(){
        Cursor sumedScores = getSumedScores();
        ListView scoresListView = findViewById(R.id.sumedScoresList);
        String[] from = {TableEntry.PLAYER_ONE , "player1Points",  "player2Points", TableEntry.PLAYER_TWO};
        int[] to = {R.id.whitePlayerName, R.id.whitePlayerResult, R.id.blackPlayerResult, R.id.blackPlayerName};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.single_list_item, sumedScores, from, to, 0);
        scoresListView.setAdapter(sca);
        scoresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView wPlayerName = (TextView) view.findViewById(R.id.whitePlayerName);
                TextView bPlayerName = (TextView) view.findViewById(R.id.blackPlayerName);


                Intent intent = new Intent(ScoreActivity.this, GameDetailsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("WhitePlayerName",  wPlayerName.getText().toString());
                bundle.putString("BlackPlayerName", bPlayerName.getText().toString());
                bundle.putBoolean("intentFromScores",  true);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    public Cursor getSumedScores (){

        SQLiteDatabase db =  helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID, " + TableEntry.PLAYER_ONE +" ,SUM("+
                TableEntry.PLAYER_ONE_POINTS +") AS player1Points, " +TableEntry.PLAYER_TWO +" ,SUM("+
                TableEntry.PLAYER_TWO_POINTS+") AS player2Points FROM " + TableEntry.TABLE_NAME +  " GROUP BY "+ TableEntry.PLAYER_ONE+ "," + TableEntry.PLAYER_TWO, null );
        return cursor;
    }


    public void onClickDeleteAllScores(View v){
        SQLiteDatabase db = helper.getWritableDatabase();
        String query ="delete from "+ TableEntry.TABLE_NAME;
        db.execSQL(query);
        db.close();
        updateScores();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
