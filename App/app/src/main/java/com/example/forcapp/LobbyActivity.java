package com.example.forcapp;

import static com.example.forcapp.MainActivity.defaultWordList;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.dao.FirebaseDAO;
import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Partida;
import com.example.forcapp.entity.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class LobbyActivity extends AppCompatActivity {

    FirebaseDAO firebaseDAO;
    private String randomWord;
    private Partida partida;
    private String partidaId;

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
        getRandomWord();
    }

    private void setLobbyUI() {
        Button signOutButton = findViewById(R.id.sign_out_bt);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            signOutButton.setVisibility(View.VISIBLE);
        else
            signOutButton.setVisibility(View.GONE);
        signOutButton.setOnClickListener(view -> {
            if (isInternetAvailable()) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });


        Button createButton = findViewById(R.id.create_bt);
        createButton.setOnClickListener(view -> {
            if (isInternetAvailable()) {
                createLobby();
            }
        });

        Button joinButton = findViewById(R.id.join_bt);
        joinButton.setOnClickListener(view -> enterCodeDialog());
    }

    private void enterCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.enter_code_dialog);

        EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(R.string.dialog_acept, (dialogInterface, i) -> {
            if (isInternetAvailable()) {
                findViewById(R.id.spinner).setVisibility(View.VISIBLE);
                partidaId = input.getText().toString();
                try {
                    partida = firebaseDAO.getGameById(partidaId);
                    if (partida==null){
                        Toast.makeText(getApplicationContext(), "Partida non atopada", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.spinner).setVisibility(View.GONE);
                        return;
                    }
                    setDatabaseListeners(firebaseDAO.databaseReference.child("Partida").child(partidaId));
                } catch (RuntimeException e) {
                    Toast.makeText(getApplicationContext(), "Problema ao cargar a partida. Inténtao de novo.", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.spinner).setVisibility(View.GONE);
                    return;
                }
                findViewById(R.id.spinner).setVisibility(View.GONE);
                if (!partida.getPlayer1().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())) {
                    if (partida != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
                        partida.setPlayer2(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                        partida.setNumPlayers(2);

                        Intent preGameIntent = new Intent(getApplicationContext(), PreGameActivity.class);
                        preGameIntent.putExtra("id", partidaId);
                        preGameIntent.putExtra("partida", partida);
                        if (!partida.isFinished() && isInternetAvailable()) {
                            firebaseDAO.updateGame(partidaId, partida);
                            startActivity(preGameIntent);
                        } else if (partida.isFinished())
                            Toast.makeText(getApplicationContext(), "Esta partida xa rematou.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Os usuarios deben ser distintos.", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, (dialogInterface, i) -> {
            // Cancelar
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void setDatabaseListeners(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (partida != null && dataSnapshot.exists())
                    partida = dataSnapshot.getValue(Partida.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro nas bases de datos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private int getRandomNumber(int max) {
        return (int) ((Math.random() * (max)) + 0);
    }

    public void getRandomWord() {
        class GetRandomWord extends AsyncTask<Void, Void, List<Word>> { // claseinterna


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                randomWord = defaultWordList.get(getRandomNumber(defaultWordList.size() - 1)).getWord();
            }

            @Override
            protected List<Word> doInBackground(Void... voids) {
                List<Word> words = WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().getAllWords();
                if (words.size() == 0) {
                    words = defaultWordList;
                }
                return words;
            }

            @Override
            protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words); // Actualizar la UI
                randomWord = words.get(getRandomNumber(words.size() - 1)).getWord();

            }

        }
        GetRandomWord gf = new GetRandomWord(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void createLobby() {
        partida = new Partida(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), randomWord, 1);
        partidaId = firebaseDAO.createGame(partida);
        Intent preGameIntent = new Intent(getApplicationContext(), PreGameActivity.class);
        preGameIntent.putExtra("id", partidaId);
        preGameIntent.putExtra("partida", partida);
        startActivity(preGameIntent);
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