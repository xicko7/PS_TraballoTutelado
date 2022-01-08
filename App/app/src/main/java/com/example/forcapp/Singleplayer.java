package com.example.forcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class Singleplayer extends AppCompatActivity implements GameActivity {

    private int intentos, numCorrectos;
    private String randomWord;
    private GridView gridView;
    private final List<ImageView> faults = new ArrayList();
    private final List<TextView> charViews = new ArrayList();
    private LinearLayout wordLayout;
    private final Collator myCollator = Collator.getInstance();
    private boolean gameFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setTitle(R.string.singleplayer_bt);

        intentos = 0;
        numCorrectos = 0;
        myCollator.setStrength(Collator.PRIMARY);
        gridView = findViewById(R.id.letters);
        LetterAdapter adapter = new LetterAdapter(getApplicationContext());
        gridView.setAdapter(adapter);
        wordLayout = findViewById(R.id.Layour_words);
        getRandomWord();
        gameFinished = false;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                if (!gameFinished) {
                    createExitDialog();
                    return true;
                } else
                    return false;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if (!gameFinished)
            createExitDialog();
        else
            finish();
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
                if (myCollator.equals(charViews.get(i).getText().toString(), letter)) {
                    correcto = true;
                    numCorrectos++;
                    charViews.get(i).setTextColor(Color.BLACK);
                }
            }

            if (correcto) {
                if (numCorrectos == randomWord.length()) {
                    disableButtons();
                    gameFinished = true;
                    createWinnerDialog();
                }
            } else if (intentos < 10) {
                faults.get(intentos).setVisibility(View.VISIBLE);
                intentos++;
            } else {
                faults.get(intentos).setVisibility(View.VISIBLE);
                disableButtons();
                gameFinished = true;
                createGameOverDialog();

            }
        }

    }

    @Override
    public void getRandomWord() {
        class GetRandomWord extends AsyncTask<Void, Void, List<Word>> { // claseinterna
            @Override
            protected List<Word> doInBackground(Void... voids) {
                List<Word> words = WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().getAllWords();
                if (words.size() == 0) {
                    words = Dictionary.defaulWordList;
                }
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
            }
        }

        GetRandomWord gf = new GetRandomWord(); // Crear una instancia y ejecutar
        gf.execute();

    }

    @Override
    public void disableButtons() {
        for (int i = 0; i < gridView.getChildCount(); i++) {
            gridView.getChildAt(i).setEnabled(false);

        }
    }

    @Override
    public void createExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit) + "?");

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.cont, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void createGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.game_over) + " " + getString(R.string.word_was) + " " + randomWord);

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

    @Override
    public void createWinnerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_winner);
        builder.setMessage(getString(R.string.word_was) + " " + randomWord);

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

}