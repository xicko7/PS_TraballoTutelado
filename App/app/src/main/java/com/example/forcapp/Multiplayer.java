package com.example.forcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class Multiplayer extends AppCompatActivity implements GameActivity {

    private int intentos, numCorrectos;
    private String randomWord;
    private GridView gridView;
    private final List<ImageView> faults = new ArrayList();
    private final List<TextView> charViews = new ArrayList();
    private LinearLayout wordLayout;
    private final Collator myCollator = Collator.getInstance();
    private boolean gameFinished;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInternetAvailable();

        setContentView(R.layout.multiplayer_lobby_layout);
        /*
         * Se non se está loggeado ofrecer log in ou sign in con un fragment (ou activity)
         */
        getSupportActionBar().setTitle(R.string.multiplayer_bt);

        setLobbyUI();


    }

    private void setLobbyUI() {
        Button createButton = findViewById(R.id.create_bt);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetAvailable()) {
                    /*
                     * Crear sala como host
                     */
                    createLobby();
                    if (isInternetAvailable())
                        startGame();
                }
            }
        });

        Button joinButton = findViewById(R.id.join_bt);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterCodeDialog();
            }
        });
    }

    private void createLobby() {
        code = getRandomNumber(0, 9999);
        /*
        String reference;
        if (code < 10)
            reference = "000" + String.valueOf(code);
        else if (code < 100)
            reference = "00" + String.valueOf(code);
        else if (code < 1000)
            reference = "0" + String.valueOf(code);
        else
            reference = String.valueOf(code);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(reference);


        myRef.setValue("Hello, World!");
         */
    }

    private void startGame() {
        setContentView(R.layout.multiplayer_layout);
        getSupportActionBar().setTitle(R.string.multiplayer_bt);

        intentos = 0;
        numCorrectos = 0;
        myCollator.setStrength(Collator.PRIMARY);
        gridView = findViewById(R.id.letters_mp);
        LetterAdapter adapter = new LetterAdapter(getApplicationContext());
        gridView.setAdapter(adapter);
        wordLayout = findViewById(R.id.layout_words_mp);
        getRandomWord();
        gameFinished = false;
        setGameUI();
    }

    private void setGameUI() {

        ArrayList<Integer> ids = new ArrayList<>();

        ids.add(R.id.fault1_mp);
        ids.add(R.id.fault2_mp);
        ids.add(R.id.fault3_mp);
        ids.add(R.id.fault4_mp);
        ids.add(R.id.fault5_mp);
        ids.add(R.id.fault6_mp);
        ids.add(R.id.fault7_mp);
        ids.add(R.id.fault8_mp);
        ids.add(R.id.fault9_mp);
        ids.add(R.id.fault10_mp);
        ids.add(R.id.fault11_mp);

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
        private final String[] letters;
        private final LayoutInflater letterInf;

        public LetterAdapter(Context context) {
            letters = new String[26];
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
        builder.setTitle(getString(R.string.game_over));
        builder.setMessage(getString(R.string.word_was) + " " + randomWord);

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.new_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*
                 * Deberiase repetir a mesma partida cos xogadores que pulsasen este botón
                 */
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
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.new_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*
                 * Deberiase repetir a mesma partida cos xogadores que pulsasen este botón
                 */
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void enterCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.enter_code_dialog);

        EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setPositiveButton(R.string.dialog_acept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                code = Integer.parseInt(String.valueOf(input.getText()));
                if (isInternetAvailable()) {
                    /*
                     * Unirse a sala con "code"
                     */
                    if (isInternetAvailable())
                        startGame();
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancelar
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isInternetAvailable() {
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean res = netInfo != null && netInfo.isConnectedOrConnecting();
        if (!res)
            Toast.makeText(getApplicationContext(), "Non hai conexión a internet.", Toast.LENGTH_SHORT).show();
        return res;
    }

}