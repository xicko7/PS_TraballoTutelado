package com.example.forcapp.dao;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Update;

import com.example.forcapp.LobbyActivity;
import com.example.forcapp.R;
import com.example.forcapp.entity.Partida;
import com.example.forcapp.entity.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


//TODO Eliminar strings estáticos e crealos comos recursos
public class FirebaseDAO {

    private final DatabaseReference databaseReference;
    private String partidaId;

    public FirebaseDAO() {
        String DATABASE_LINK = "https://forcapp-bc7d8-default-rtdb.europe-west1.firebasedatabase.app/";
        FirebaseDatabase db = FirebaseDatabase.getInstance(DATABASE_LINK);
        databaseReference = db.getReference();
    }

    public void removeGame(String id) {
        databaseReference.child("Partida").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Realtime", "Eliminado correctamente");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Realtime", "Error ao eliminar: " + e);
            }
        });
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
                partidaMap.put("isFinished", partida.isFinished());
                partidaMap.put("repeat1", partida.isRepeat1());
                partidaMap.put("repeat2", partida.isRepeat2());
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

                databaseReference.child("Partida").child(partidaId).child("isFinished").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.getValue().equals(true))
                                removeGame(partidaId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return partidaId;
            }
        }
        CreateGame gf = new CreateGame();
        gf.execute();
        return partidaId;
    }



    public Partida getGameById(String partidaId) {
        return databaseReference.child("Partida").child(partidaId).get().getResult().getValue(Partida.class);
    }


    public void updateGame(String partidaId, Map<String, Object> hashMap) {
        class UpdateGame extends AsyncTask<Void, Void, Void> { // claseinterna
            @Override
            protected Void doInBackground(Void... voids) {
                databaseReference.child("Partida").child(partidaId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Realtime", "Datos actualizados correctamente");
                    }
                });
                return null;
            }

        }
        UpdateGame gf = new UpdateGame();
        gf.execute();
    }


}
