package com.example.forcapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.util.ArrayList;
import java.util.List;

public class Singleplayer extends AppCompatActivity {

    private LetterAdapter adapter;
    private GridView gridView;
    private List<ImageView> faults = new ArrayList();
    private int intentos = 0;
    private String randomWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        gridView = findViewById(R.id.letters);
        adapter = new LetterAdapter(getApplicationContext());
        gridView.setAdapter(adapter);
        getRandomWord();

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

    void getRandomWord() {
        class GetRandomWord extends AsyncTask<Void, Void, List<Word>> { // claseinterna
            @Override
            protected List<Word> doInBackground(Void... voids) {
                List<Word> words = WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().getAllWords();
                return words;
            }

            @Override
            protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words); // Actualizar la UI
                randomWord = words.get(getRandomNumber(0, words.size() - 1)).getWord();
                Toast.makeText(getApplicationContext(), randomWord, Toast.LENGTH_SHORT).show();
            }
        }

        GetRandomWord gf = new GetRandomWord(); // Crear una instancia y ejecutar
        gf.execute();

    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private class LetterAdapter extends BaseAdapter {
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
            Toast.makeText(getApplicationContext(), "Pulsada letra " + letter, Toast.LENGTH_SHORT).show();
            view.setEnabled(false);
            view.setVisibility(View.GONE);

            if (intentos < 11) {
                faults.get(intentos).setVisibility(View.VISIBLE);
                intentos++;
            }
        }

    }

}