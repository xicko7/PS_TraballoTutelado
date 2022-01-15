package com.example.forcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.dao.FirebaseDAO;
import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Partida;
import com.example.forcapp.entity.Word;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    FirebaseDAO firebaseDAO;
    private String randomWord;
    public Partida partida;
    public static String partidaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent authIntent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(authIntent);
        }
        getRandomWord();
        firebaseDAO = new FirebaseDAO();

        setContentView(R.layout.multiplayer_lobby_layout);

        setLobbyUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        }
        if (partida != null && partida.isFinished()) {
            firebaseDAO.removeGame();
        }
    }

    private void setLobbyUI() {
        Button signOutButton = findViewById(R.id.sign_out_bt);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            signOutButton.setVisibility(View.VISIBLE);
        else
            signOutButton.setVisibility(View.GONE);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetAvailable()) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                }
            }
        });


        Button createButton = findViewById(R.id.create_bt);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetAvailable()) {
                    /*
                     * Crear sala como host
                     */
                    createLobby();
                    //Por ahora que vaya al preGame
                    // if (isInternetAvailable())
                    //startGame();


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

    private void enterCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.enter_code_dialog);

        EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(R.string.dialog_acept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //code = Integer.parseInt(String.valueOf(input.getText()));
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

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

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

            }

        }
        GetRandomWord gf = new GetRandomWord(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void createLobby() {

        partida = new Partida(FirebaseAuth.getInstance().getCurrentUser().getEmail(), randomWord, 1);
        partidaId = firebaseDAO.createGame(partida);
        Intent preGameIntent = new Intent(getApplicationContext(), PreGameActivity.class);
        startActivity(preGameIntent);
    }

    private void startGame() {
        /*
        Intent + bundle
         */

        Intent multiplayerIntent = new Intent(getApplicationContext(), Multiplayer.class);
        /*
        put extras
         */
        finish();
        startActivity(multiplayerIntent);
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