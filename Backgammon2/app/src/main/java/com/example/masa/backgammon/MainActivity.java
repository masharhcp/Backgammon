package com.example.masa.backgammon;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    private static int THRESHOLD = 10;
    private static int BETWEEN_SHAKES = 400;
    private static int DIRECTION_CHANGES = 3;
    private static int PAUSE = 1000;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        Button continueButton = (Button) findViewById(R.id.continueGameButton);
//        File dir = getFilesDir();
//        File file = new File(dir, "Saved_Game");
//        String path = file.getPath();
//        System.out.println("PUTANJICA JE "+path);

        Path p = Paths.get("/data/user/0/com.example.masa.backgammon/files/Saved_Game");
        boolean exists = Files.exists(p);
        if(exists){
            continueButton.setEnabled(true);
            continueButton.setBackgroundTintList(null);
            continueButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        }
        else {
            continueButton.setEnabled(false);
            continueButton.setBackgroundTintList(null);
            continueButton.setBackgroundTintList(getResources().getColorStateList(R.color.black_overlay));
        }

        setSharedPreferences();

    }

    private void setSharedPreferences() {
//        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        edit.putInt(R.string.thresholdDefault, );
//        edit.putInt(R.string.thresholdCurrent);
//        edit.putInt(R.string.betweenShakesDefault);
//        edit.putInt(R.string.betweenShakesCurrent);
//        edit.putInt(R.string.directionChangesDefault);
//        edit.putInt(R.string.directionChangesCurrent);
//        edit.putInt(R.string.pauseDefault);
//        edit.putInt(R.string.pauseDefault);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button continueButton = (Button) findViewById(R.id.continueGameButton);
        FileInputStream fis = null;
        try {
            fis = this.openFileInput("Saved_Game");
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        if(fis!=null){
            continueButton.setEnabled(true);
            continueButton.setBackgroundTintList(null);
            continueButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));


        }
        else {
            continueButton.setEnabled(false);
            continueButton.setBackgroundTintList(null);
            continueButton.setBackgroundTintList(getResources().getColorStateList(R.color.black_overlay));
        }
    }

    public void onClickStartGame(View v) {
        Intent intent = new Intent(this, PlayerInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("ContinueGame", false);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    public void onClickContnueGame(View v){
        Intent intent = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("ContinueGame", true);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    public void onClickScores(View v){
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSettings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

}
