package com.example.masa.backgammon;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, true);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        int threshold = sharedPreferences.getInt("currentThreshold", 0);
        int betweenShakes = sharedPreferences.getInt("currentBetweenShakes", 0);
        int pause = sharedPreferences.getInt("currentPause", 0);
        int directionChanges = sharedPreferences.getInt("currentDirectionChanges", 0);
        TextView thres =(TextView) findViewById(R.id.threshold);
        TextView pauseV = (TextView)findViewById(R.id.pauseBetweenMovements);
        TextView minDir =(TextView) findViewById(R.id.minDirectionChanges);
        TextView timeBetw = (TextView) findViewById(R.id.timeBetweenShakes);
        thres.setText(""+threshold);
        pauseV.setText(""+pause);
        minDir.setText(""+directionChanges);
        timeBetw.setText(""+betweenShakes);
    }

    public void onRestoreToDefault(View view){
        PreferenceManager.setDefaultValues(this, R.xml.prefs, true);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int threshold = Integer.parseInt(sharedPreferences.getString("currentThreshold", "10"));
        int betweenShakes = Integer.parseInt(sharedPreferences.getString("currentBetweenShakes", "400"));
        int pause = Integer.parseInt(sharedPreferences.getString("currentPause", "1000"));
        int directionChanges = Integer.parseInt(sharedPreferences.getString("currentDirectionChanges", "3"));
        SharedPreferences sharedPreferences1 = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putInt("currentThreshold", threshold);
        editor.putInt("currentPause", pause);
        editor.putInt("currentDirectionChanges", directionChanges);
        editor.putInt("currentBetweenShakes", betweenShakes);
        editor.commit();
        ShakeSensor.setShakeTreshold(threshold);
        ShakeSensor.setShakeTimeout(pause);
        ShakeSensor.setMinDirectionChange(directionChanges);
        ShakeSensor.setMinTimeBetweenShakes(betweenShakes);
        TextView thres =(TextView) findViewById(R.id.threshold);
        TextView pauseV = (TextView)findViewById(R.id.pauseBetweenMovements);
        TextView minDir =(TextView) findViewById(R.id.minDirectionChanges);
        TextView timeBetw = (TextView) findViewById(R.id.timeBetweenShakes);
        thres.setText(""+threshold);
        pauseV.setText(""+pause);
        minDir.setText(""+directionChanges);
        timeBetw.setText(""+betweenShakes);


    }

    public void changePreferences(View view){
        EditText thres = (EditText) findViewById(R.id.changedThreshold);
        EditText pauseV = (EditText)findViewById(R.id.changedPauseBetweenMovements);
        EditText minDir = (EditText)findViewById(R.id.changedMinimumDirectionChanges);
        EditText timeBetw = (EditText)findViewById(R.id.changedTimeBetweenShakes);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(((thres.getText().toString())!=null) && !(thres.getText().toString()).equals("")) {
            int text = Integer.parseInt((thres.getText()).toString());
            editor.putInt("currentThreshold", text);
            TextView thresV = (TextView) findViewById(R.id.threshold);
            thresV.setText(""+text);
            ShakeSensor.setShakeTreshold(text);

        }

        if (((pauseV.getText()).toString()!=null) && !(pauseV.getText()).toString().equals("")){
        int p = Integer.parseInt((pauseV.getText()).toString());
            editor.putInt("currentPause", p);
            TextView pauseVV = (TextView)findViewById(R.id.pauseBetweenMovements);

            pauseVV.setText(""+p);
            ShakeSensor.setShakeTimeout(p);
        }

        if (((minDir.getText()).toString()!=null) && !(minDir.getText()).toString().equals("")) {
            int min = Integer.parseInt((minDir.getText()).toString());
            editor.putInt("currentDirectionChanges", min);
            TextView minDirV =(TextView) findViewById(R.id.minDirectionChanges);
            minDirV.setText(""+min);
            ShakeSensor.setMinDirectionChange(min);
        }
        if (((timeBetw.getText()).toString()!=null) && !(timeBetw.getText()).toString().equals("")){
        int time = Integer.parseInt((timeBetw.getText()).toString());
        editor.putInt("currentBetweenShakes", time);
        TextView timeBetwV = (TextView) findViewById(R.id.timeBetweenShakes);
        timeBetwV.setText(""+time);
        ShakeSensor.setMinTimeBetweenShakes(time);
        }
        editor.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
