package com.example.masa.backgammon;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class PlayerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);

        final RadioGroup radio = (RadioGroup) findViewById(R.id.radioGroup);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radio.findViewById(checkedId);
                int index = radio.indexOfChild(radioButton);

                // Add logic here
                EditText whitePlayer = (EditText) findViewById(R.id.whitePlayerName);
                EditText blackPlayer = (EditText) findViewById(R.id.blackPlayerName);
                switch (index) {


                    case 0: // secondbutton

                        whitePlayer.setText("Player1");
                        whitePlayer.setEnabled(true);
                        blackPlayer.setText("Computer");
                        blackPlayer.setEnabled(false);

                        break;
                    case 1:

                        whitePlayer.setText("Player1");
                        whitePlayer.setEnabled(true);
                        blackPlayer.setText("Player2");
                        blackPlayer.setEnabled(true);

                        break;

                }
            }
        });
    }

    public void onClickStartGame(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        EditText white = (EditText)findViewById(R.id.whitePlayerName);
        EditText black = (EditText)findViewById(R.id.blackPlayerName);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        int checkedRadioButtonId = rg.getCheckedRadioButtonId();
        int comp = 2;
        switch (checkedRadioButtonId){

            case R.id.blackIsComp: comp = 1; break;
            case R.id.noComp: comp = 2; break;

        }

        Bundle bundle = new Bundle();
        bundle.putString("WhitePlayerName", white.getText().toString());
        if ((white.getText().toString()).equals(black.getText().toString())){
            bundle.putString("BlackPlayerName", black.getText().toString().concat("1"));
        }
        else{
        bundle.putString("BlackPlayerName", black.getText().toString());
        }
        bundle.putInt("ComputerPlayer", comp);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
