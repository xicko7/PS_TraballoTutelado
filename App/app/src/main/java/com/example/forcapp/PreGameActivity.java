package com.example.forcapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.forcapp.dao.FirebaseDAO;
import com.example.forcapp.entity.Partida;
import com.google.firebase.auth.FirebaseAuth;

public class PreGameActivity extends AppCompatActivity {

    Button boton_listo;
    Partida partida;
    private String partidaId;
    private FirebaseDAO firebaseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("id") != null)
            partidaId = getIntent().getStringExtra("id");

        Bundle bundle = getIntent().getExtras();
        if (bundle.getParcelable("partida") != null)
            partida = (Partida) bundle.getParcelable("partida");


        setContentView(R.layout.activity_pre_game);
        firebaseDAO = new FirebaseDAO();
        setUI();
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

        //Id de la partida
        TextView tv_partidaID = findViewById(R.id.partidaId_tv);
        tv_partidaID.setText(partidaId);
        tv_partidaID.setVisibility(View.VISIBLE);

        TextView tv_emailPlayer2;
        if (!partida.getPlayer2().equals("null")) {
            // email jugador 1
            tv_emailPlayer2 = findViewById(R.id.emailPlayer2_tv);
            tv_emailPlayer2.setText(partida.getPlayer2());
            tv_emailPlayer2.setVisibility(View.VISIBLE);
        }

        boton_listo = findViewById(R.id.boton_listo);
        TextView readyPlayer1;
        TextView readyPlayer2;

        readyPlayer1 = findViewById(R.id.ready_player1);
        readyPlayer2 = findViewById(R.id.ready_player2);

        boton_listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(partida.getPlayer1())) {
                    partida.setReadyPlayer1(true);
                    boton_listo.setEnabled(false);
                    readyPlayer1.setVisibility(View.VISIBLE);
                    readyPlayer1.setText(partida.getPlayer1());
                } else {
                    partida.setReadyPlayer2(true);
                    boton_listo.setEnabled(false);
                    readyPlayer2.setVisibility(View.VISIBLE);
                    readyPlayer2.setText(partida.getPlayer2());
                }

            }
        });

    }

    public void createExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit) + "?");

        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                partida.setFinished(true);
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

}