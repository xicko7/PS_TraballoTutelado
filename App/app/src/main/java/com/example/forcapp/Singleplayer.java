package com.example.forcapp;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Singleplayer extends AppCompatActivity {

    private LetterAdapter adapter;
    private GridView gridView;
    static List<ImageView> faults = new ArrayList();
    static int intentos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gridView = findViewById(R.id.letters);

        adapter = new LetterAdapter(getApplicationContext());
        gridView.setAdapter(adapter);

        setUI();
    }

    private void setUI() {

        ArrayList<Integer> ids = new ArrayList<Integer>();

        ids.add(R.id.fault1);
        ids.add(R.id.fault2);
        ids.add(R.id.fault3);
        ids.add(R.id.fault4);
        ids.add(R.id.fault5);
        ids.add(R.id.fault6);
        ids.add(R.id.fault7);
        ids.add(R.id.fault8);
        ids.add(R.id.fault9);
        ids.add(R.id.fault10);
        ids.add(R.id.fault11);

        for (int i = 0; i < ids.size(); i++) {
            ImageView imageViewIcon = findViewById(ids.get(i));
            imageViewIcon.setColorFilter(getApplicationContext().getResources().getColor(R.color.white));

            faults.add(imageViewIcon);

        }
    }

    static void tapLetter(String letter, View view) {
        Toast.makeText(view.getContext(), "Pulsada letra " + letter, Toast.LENGTH_SHORT).show();
        view.setEnabled(false);
        view.setVisibility(View.GONE);

        faults.get(intentos).setVisibility(View.VISIBLE);
        intentos++;

    }
}