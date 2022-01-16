package com.example.forcapp;

import static android.content.Intent.ACTION_SEND;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forcapp.dao.FirebaseDAO;
import com.example.forcapp.entity.Partida;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;

public class PreGameActivity extends AppCompatActivity {

    Button boton_listo;
    Partida partida;
    private String partidaId;
    private FirebaseDAO firebaseDAO;
    private boolean hosting = true; //Player1
    private TextView readyPlayer1;
    private TextView readyPlayer2;
    private TextView tv_emailPlayer2;
    private boolean buttonEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("id") != null)
            partidaId = getIntent().getStringExtra("id");
        else
            finish();

        Bundle bundle = getIntent().getExtras();
        if (bundle.getParcelable("partida") != null)
            partida = bundle.getParcelable("partida");
        else
            finish();

        buttonEnabled = true;
        setContentView(R.layout.activity_pre_game);
        firebaseDAO = new FirebaseDAO();
        setDatabaseListeners(firebaseDAO.databaseReference.child("Partida").child(partidaId));
        setUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pre_game_layout, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu:
                Intent intentSend = new Intent(ACTION_SEND);
                intentSend.setType("text/plain");
                intentSend.putExtra(Intent.EXTRA_TEXT, partidaId);
                startActivity(intentSend);
                return true;
            case android.R.id.home:
                partida.setFinished(true);
                firebaseDAO.updateGame(partidaId, partida);
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        createExitDialog();
    }

    private void setUI() {
        //email jugador 1
        TextView tv_emailPlayer1 = findViewById(R.id.emailPlayer1_tv);
        tv_emailPlayer1.setText(partida.getPlayer1());
        tv_emailPlayer1.setVisibility(View.VISIBLE);

        tv_emailPlayer2 = findViewById(R.id.emailPlayer2_tv);
        readyPlayer1 = findViewById(R.id.ready_player1);
        readyPlayer2 = findViewById(R.id.ready_player2);

        //Id de la partida
        TextView tv_partidaID = findViewById(R.id.partidaId_tv);
        tv_partidaID.setText(partidaId);
        tv_partidaID.setVisibility(View.VISIBLE);


        boton_listo = findViewById(R.id.boton_listo);

        readyPlayer1.setText(partida.getPlayer1());

        if (!partida.getPlayer2().equals("null")) {
            // xogador 2
            hosting = false;
            tv_emailPlayer2.setText(partida.getPlayer2());
            tv_emailPlayer2.setVisibility(View.VISIBLE);
            readyPlayer2.setText(partida.getPlayer2());
            if (partida.isReadyPlayer1())
                readyPlayer1.setVisibility(View.VISIBLE);
        }


        boton_listo.setOnClickListener(view -> {
            if (hosting) {
                partida.setReadyPlayer1(true);
                firebaseDAO.player1Ready(partidaId);
            } else {
                partida.setReadyPlayer2(true);
                firebaseDAO.player2Ready(partidaId);
            }
            refreshUI(partida);
            boton_listo.setEnabled(false);
        });
        if (hosting)
            boton_listo.setEnabled(false);


    }

    private void refreshUI(Partida partida) {
        if (partida.isReadyPlayer1())
            readyPlayer1.setVisibility(View.VISIBLE);
        if (partida.isReadyPlayer2())
            readyPlayer2.setVisibility(View.VISIBLE);

        if (partida.isReadyPlayer1() && this.partida.isReadyPlayer2())
            startGame();
    }

    public void createExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit) + "?");

        builder.setPositiveButton(R.string.exit, (dialogInterface, i) -> {
            partida.setFinished(true);
            if (partida.getPlayer1() != null)
                firebaseDAO.updateGame(partidaId, partida);
            finish();
        });
        builder.setNegativeButton(R.string.cont, (dialogInterface, i) -> {

        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void startGame() {
        try {
            partida = firebaseDAO.getGameById(partidaId);
            Intent multiplayerIntent = new Intent(getApplicationContext(), Multiplayer.class);
            multiplayerIntent.putExtra("id", partidaId);
            multiplayerIntent.putExtra("partida", partida);
            finish();
            startActivity(multiplayerIntent);
        } catch (InterruptedException e) {
            e.printStackTrace();
            partida.setFinished(true);
            firebaseDAO.updateGame(partidaId, partida);
            finish();
        }
    }

    void setDatabaseListeners(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (partida.isReadyPlayer1() && partida.isReadyPlayer2())
                    startGame();
                if (dataSnapshot.exists()) {
                    partida = dataSnapshot.getValue(Partida.class);
                    if (partida != null) {
                        if (!partida.getPlayer2().equals("null")) {
                            if (buttonEnabled) {
                                boton_listo.setEnabled(true);
                                buttonEnabled = false;
                            }
                        }
                        refreshUI(partida);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro nas bases de datos", Toast.LENGTH_SHORT).show();
            }
        });

        ref.child("isReadyPlayer1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getValue().equals(true)) {
                        partida.setReadyPlayer1(true);
                        firebaseDAO.player1Ready(partidaId);
                        if (partida.isReadyPlayer1())
                            readyPlayer1.setVisibility(View.VISIBLE);
                        if (readyPlayer2.getVisibility() == View.VISIBLE && readyPlayer1.getVisibility() == View.VISIBLE)
                            startGame();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erro nas bases de datos", Toast.LENGTH_SHORT).show();
            }
        });

        ref.child("isReadyPlayer2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getValue().equals(true)) {
                        partida.setReadyPlayer2(true);
                        firebaseDAO.player2Ready(partidaId);
                        if (partida.isReadyPlayer2()) {
                            readyPlayer2.setText(partida.getPlayer2());
                            readyPlayer2.setVisibility(View.VISIBLE);
                        }
                        if (readyPlayer2.getVisibility() == View.VISIBLE && readyPlayer1.getVisibility() == View.VISIBLE)
                            startGame();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erro nas bases de datos", Toast.LENGTH_SHORT).show();
            }
        });

        ref.child("player2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (!snapshot.getValue().equals("null")) {
                        partida.setPlayer2((String) snapshot.getValue());
                        tv_emailPlayer2.setText(partida.getPlayer2());
                        tv_emailPlayer2.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erro nas bases de datos", Toast.LENGTH_SHORT).show();
            }
        });

    }

}