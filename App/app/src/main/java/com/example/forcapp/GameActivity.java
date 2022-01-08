package com.example.forcapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.forcapp.entity.Word;

public interface GameActivity {

    void getRandomWord();

    void disableButtons();

    default int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    void createExitDialog();

    void createGameOverDialog();

    void createWinnerDialog();

}
