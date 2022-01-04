package com.example.forcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class Singleplayer extends AppCompatActivity {

    private LetterAdapter adapter;
    private GridView gridView;
    private List<ImageView> faults = new ArrayList();
    private int intentos;
    private String randomWord;
    private List<TextView> charViews = new ArrayList();
    private LinearLayout wordLayout;
    private int numCorrectos;
    private Collator myColaltor = Collator.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setTitle(R.string.singleplayer_bt);

        intentos = 0;
        numCorrectos = 0;
        myColaltor.setStrength(Collator.PRIMARY);
        gridView = findViewById(R.id.letters);
        adapter = new LetterAdapter(getApplicationContext());
        gridView.setAdapter(adapter);
        wordLayout = findViewById(R.id.Layour_words);
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

                for (int i = 0; i < randomWord.length(); i++) {
                    charViews.add(new TextView(getApplicationContext()));
                    charViews.get(i).setBackgroundResource(R.drawable.guionbajo_letra);
                    charViews.get(i).append(String.valueOf(randomWord.charAt(i)).toUpperCase());

                    charViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    charViews.get(i).setGravity(Gravity.CENTER);
                    charViews.get(i).setTextColor(Color.WHITE);
                    charViews.get(i).setTextSize(25);
                    wordLayout.addView(charViews.get(i));
                }
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
                    if (numCorrectos != randomWord.length()) {
                        tapLetter(letters[i], view);
                    } else {
                        createWinnerDialog();
                    }
                }
            });
            buttonLetter.setText(letters[i]);
            return buttonLetter;
        }

        void tapLetter(String letter, View view) {
            //Toast.makeText(getApplicationContext(), "Pulsada letra " + letter, Toast.LENGTH_SHORT).show();
            view.setEnabled(false);
            view.setVisibility(View.GONE);

            boolean correcto = false;


            for (int i = 0; i < randomWord.length(); i++) {
                if (myColaltor.equals(charViews.get(i).getText().toString(), letter)) {
                    correcto = true;
                    numCorrectos++;
                    charViews.get(i).setTextColor(Color.BLACK);
                }
            }

            if (correcto) {
                if (numCorrectos == randomWord.length()) {
                    disableButtons();
                    createWinnerDialog();
                }
            } else if (intentos < 10) {
                faults.get(intentos).setVisibility(View.VISIBLE);
                intentos++;
            } else {
                faults.get(intentos).setVisibility(View.VISIBLE);
                disableButtons();
                createGameOverDialog();

            }
        }

    }

    private void createGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.gameOver);

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.new_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createWinnerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.gameWinner);
        builder.setMessage(getString(R.string.gameWinnerMessage) + ": " + randomWord);

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.new_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void disableButtons() {
        for (int i = 0; i < gridView.getChildCount(); i++) {
            gridView.getChildAt(i).setEnabled(false);

        }
    }

}