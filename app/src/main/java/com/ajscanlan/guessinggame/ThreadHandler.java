package com.ajscanlan.guessinggame;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander Scanlan on 25/11/2015
 */
public class ThreadHandler implements Runnable {

    static int numberLimit = 1000;

    private int mPlayer;

    private int mNumberToGuess;
    private List<Integer> numberList;
    private Random randy = new Random();
    private ThreadCallback mCallback;

    public ThreadHandler(int numberToGuess, ThreadCallback callback, int player){
        mNumberToGuess = numberToGuess;
        numberList = new ArrayList<>();
        mCallback = callback;

        mPlayer = player;

        //populates list with 1-100
        for(int i = 0; i <= numberLimit; ++i){
            numberList.add(i);
        }
    }

    @Override
    public void run() {
        int guess;
        int randomNumber;

        for(int i = 0; i <= numberLimit; ++i){
            randomNumber = randy.nextInt(numberList.size());
            guess = numberList.get(randomNumber);

            //remove guessed number from list
            numberList.remove(randomNumber);

            Log.d("DEBUGTAG", guess + "");

            if(guess == mNumberToGuess){
                Log.d("DEBUGTAG", "NUMBER GUESSED SUCCESSFULLY: " + guess + ", PLAYER:" + mPlayer);
                mCallback.winner(guess, mPlayer);
                break;
            }
        }

    }
}
