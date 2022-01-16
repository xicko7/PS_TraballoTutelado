package com.example.forcapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.dao.FirebaseDAO;
import com.example.forcapp.entity.Partida;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Multiplayer extends AppCompatActivity implements GameActivity {

    private int intentos, numCorrectos;
    private String randomWord, partidaId;
    private GridView gridView;
    private List<ImageView> faults;
    private List<TextView> charViews;
    private LinearLayout wordLayout;
    private final Collator myCollator = Collator.getInstance();
    private boolean gameFinished;
    private Partida partida;
    private FirebaseDAO firebaseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("id") != null)
            partidaId = getIntent().getStringExtra("id");
        else
            goHome();

        Bundle bundle = getIntent().getExtras();
        if (bundle.getParcelable("partida") != null)
            partida = bundle.getParcelable("partida");
        else
            goHome();
        firebaseDAO = new FirebaseDAO();
        setDatabaseListeners(firebaseDAO.databaseReference.child("Partida").child(partidaId));

        if (!isInternetAvailable()) {
            partida.setFinished(true);
            firebaseDAO.updateGame(partidaId, partida);
            goHome();
        }
        setContentView(R.layout.multiplayer_layout);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.multiplayer_bt);

        faults = new ArrayList();
        charViews = new ArrayList();

        setGameUI();
        startGame();
    }

    private void startGame() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.multiplayer_bt);

        intentos = 0;
        numCorrectos = 0;
        myCollator.setStrength(Collator.PRIMARY);
        gridView = findViewById(R.id.letters_mp);
        LetterAdapter adapter = new LetterAdapter(getApplicationContext());
        gridView.setAdapter(adapter);
        wordLayout = findViewById(R.id.layout_words_mp);
        gameFinished = false;
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

        randomWord = partida.getRandomWord();
        wordLayout = findViewById(R.id.layout_words_mp);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Respond to the action bar's Up/Home button
            if (!gameFinished) {
                createExitDialog();
                return true;
            } else
                return false;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (!gameFinished)
            createExitDialog();
        else{
            partida.setFinished(true);
            firebaseDAO.updateGame(partidaId, partida);
            finish();
        }
    }

    void setDatabaseListeners(DatabaseReference ref){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                partida = dataSnapshot.getValue(Partida.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro nas bases de datos", Toast.LENGTH_SHORT).show();
            }
        });

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
            buttonLetter.setOnClickListener(view1 -> {
                if (numCorrectos != randomWord.length()) {
                    tapLetter(letters[i], view1);
                } else {
                    createWinnerDialog();
                }
            });
            buttonLetter.setText(letters[i]);
            return buttonLetter;
        }

        void tapLetter(String letter, View view) {
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
    public void disableButtons() {
        for (int i = 0; i < gridView.getChildCount(); i++) {
            gridView.getChildAt(i).setEnabled(false);

        }
    }

    @Override
    public void createExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit) + "?");

        builder.setPositiveButton(R.string.exit, (dialogInterface, i) -> {
            partida.setFinished(true);
            firebaseDAO.updateGame(partidaId, partida);
            finish();
        });
        builder.setNegativeButton(R.string.cont, (dialogInterface, i) -> {

        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void createGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.game_over));
        builder.setMessage(getString(R.string.word_was) + " " + randomWord);

        builder.setPositiveButton(R.string.exit, (dialogInterface, i) -> {
            partida.setFinished(true);
            firebaseDAO.updateGame(partidaId, partida);
            finish();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void createWinnerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_winner);
        builder.setMessage(getString(R.string.word_was) + " " + randomWord);

        builder.setPositiveButton(R.string.exit, (dialogInterface, i) -> {
            partida.setFinished(true);
            firebaseDAO.updateGame(partidaId, partida);
            finish();
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

    private void goHome() {
        Toast.makeText(getApplicationContext(), "Erro ao iniciar a partida.", Toast.LENGTH_SHORT).show();
        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        partida.setFinished(true);
        firebaseDAO.updateGame(partidaId, partida);
        finish();
        startActivity(homeIntent);
    }

}