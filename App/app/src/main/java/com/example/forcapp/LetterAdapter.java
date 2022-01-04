package com.example.forcapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

public class LetterAdapter extends BaseAdapter {
    private String[] letters;
    private LayoutInflater letterInf;
    private Context context;

    public LetterAdapter(Context context) {
        letters = new String[26];
        this.context = context;
        for (int i = 0; i < letters.length; i++) {
            letters[i] = "" + (char) (i + 'A');
        }
        letterInf = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button buttonLetter;
        if (view == null) {
            buttonLetter = (Button) letterInf.inflate(R.layout.letters, viewGroup, false);
        } else {
            buttonLetter = (Button) view;
        }
        buttonLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapLetter(letters[i], view);
            }
        });
        buttonLetter.setText(letters[i]);
        return buttonLetter;
    }

    void tapLetter(String letter, View view) {
        // Función executada ao pulsar o botón
        Toast.makeText(context, "Pulsada letra " + letter, Toast.LENGTH_SHORT).show();
        view.setEnabled(false);
        //Comprobar si la letrá está en la palabra

    }
}
