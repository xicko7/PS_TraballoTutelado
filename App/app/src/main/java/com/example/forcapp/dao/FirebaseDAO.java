package com.example.forcapp.dao;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.forcapp.WordsAdapter;
import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Partida;
import com.example.forcapp.entity.Users;
import com.example.forcapp.entity.Word;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDAO {

    private DatabaseReference databaseReference;
    private String partidaId;
    private final String DATABASE_LINK = "https://forcapp-bc7d8-default-rtdb.europe-west1.firebasedatabase.app/";
    public FirebaseDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(DATABASE_LINK);
        databaseReference = db.getReference();
    }

    public void addUser(Users user) {
        class AddUser extends AsyncTask<Void, Void, Users> { // claseinterna
            @Override
            protected Users doInBackground(Void... voids) {
                databaseReference.child("Users").push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Realtime", "Añadido correctamente");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Realtime", "Error al añadir: " + e);
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Users user) {
                super.onPostExecute(user);
            }
        }
        AddUser gf = new AddUser();
        gf.execute();
    }

    /*TODO Hay que facer que este método devolva a partidaId para ser máis facil para pasarlla á Actividad de PREGAME */
    public String createGame(Partida partida) {

        class CreateGame extends AsyncTask<Void, Void, String> { // claseinterna


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                partidaId = databaseReference.child("Partida").push().getKey();
            }

            @Override
            protected String doInBackground(Void... voids) {
                Map<String, Object> partidaMap = new HashMap<>();
                partidaMap.put("player1", partida.getPlayer1());
                partidaMap.put("player2", partida.getPlayer2());
                partidaMap.put("randomWord", partida.getRandomWord());
                partidaMap.put("letrasAcertadas1", partida.getLetrasAcertadas1());
                partidaMap.put("letrasAcertadas2", partida.getLetrasAcertadas2());
                partidaMap.put("ganador", partida.getGanador());
                partidaMap.put("numPlayers", partida.getNumPlayers());
                partidaMap.put("isReadyPLayer1", partida.isReadyPlayer1());
                partidaMap.put("isReadyPlayer2", partida.isReadyPlayer2());
                databaseReference.child("Partida").child(partidaId).updateChildren(partidaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Realtime", "Partida creada correctamente.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Realtime", "Error al añadir: " + e);
                    }
                });
                return partidaId;
            }
        }
        CreateGame gf = new CreateGame();
        gf.execute();
        return partidaId;
    }


}
