package com.ajscanlan.guessinggame;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements ThreadCallback {

    private TextView tv;
    private Random randy = new Random();

    private int playerOneNumber = 0;
    private int playerTwoNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
        //hide keyboard
        hideSoftKeyboard();

        //reset winner text view
        tv = (TextView) findViewById(R.id.winnerTextView);
        tv.setText("");

        //Set the number limit
        try {
            ThreadHandler.numberLimit = Integer.parseInt(((EditText) findViewById(R.id.edit_number_limit)).getText().toString());
        } catch (NumberFormatException e) {
            ThreadHandler.numberLimit = 1000;
        }

        //get number to guess from EditText and parse to int. Defaults to 1 if nothing entered
        try {
            playerOneNumber = Integer.parseInt(((EditText) findViewById(R.id.edit_text_1)).getText().toString());
        } catch (NumberFormatException e) {
            playerOneNumber = 1;
        }

        try{
            playerTwoNumber = Integer.parseInt(((EditText) findViewById(R.id.edit_text_2)).getText().toString());
        }catch(NumberFormatException e){
            playerTwoNumber = 1;
        }

        //Construct the thread handlers
        Runnable playerOneThread = new ThreadHandler(playerOneNumber, this, 1);
        Runnable playerTwoThread = new ThreadHandler(playerTwoNumber, this, 2);

        //Start the threads randomly
        int randomThread = randy.nextInt(1);
        if (randomThread == 0) {
            new Thread(playerOneThread).start();
            new Thread(playerTwoThread).start();
        } else {
            new Thread(playerTwoThread).start();
            new Thread(playerOneThread).start();
        }
    }


    @Override
    public void winner(final int winningNum, final int winningPlayer) {

        //Set color of text to display
        int color = Color.BLACK;

        if (winningPlayer == 1) {
            color = Color.BLUE;
        } else {
            color = Color.RED;
        }

        Log.d("WINNERDEBUG", winningPlayer + ", " + winningNum);

        final int finalColor = color;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tv.getText().length() == 0) {
                    tv.setTextColor(finalColor);
                    tv.setText("Winning Player: " + winningPlayer + ", Winning Number: " + winningNum);

                    if(winningPlayer == 1){
                        MediaPlayer mMediaPlayer = new MediaPlayer();
                        mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.player_1);
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.start();
                    } else if(winningPlayer == 2){
                        MediaPlayer mMediaPlayer = new MediaPlayer();
                        mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.player_2);
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.start();
                    }
                }
            }
        });
    }

    /**
     * Hides the soft keyboard. THANKS STACK OVERFLOW
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
