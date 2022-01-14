package com.example.forcapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent authIntent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(authIntent);
        }
/*        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
        }*/

        setContentView(R.layout.multiplayer_lobby_layout);

        setLobbyUI();
    }

/*    @Override
    protected void onRestart() {
        super.onRestart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
        }
    }*/

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

    private void createLobby() {
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