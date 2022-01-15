package com.example.forcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.forcapp.dao.FirebaseDAO;

public class PreGameActivity extends AppCompatActivity {
    private TextView tv_emailPlayer1, tv_partidaID ;

    Button boton_listo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);

        setUI();
    }

    private void setUI(){
        //email jugador 1
        tv_emailPlayer1 = findViewById(R.id.emailPlayer1_tv);
        tv_emailPlayer1.setText(LobbyActivity.partida.getPlayer1());

        //Id de la partida
        tv_partidaID= findViewById(R.id.partidaId_tv);
        tv_partidaID.setText(LobbyActivity.partidaId);
        tv_partidaID.setVisibility(View.VISIBLE);

        boton_listo = findViewById(R.id.boton_listo);
        boton_listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LobbyActivity.partida.isReadyPlayer1();
                boton_listo.setEnabled(false);
            }
        });

    }
}