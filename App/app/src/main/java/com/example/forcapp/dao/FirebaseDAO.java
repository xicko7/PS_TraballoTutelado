package com.example.forcapp.dao;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.forcapp.entity.Partida;
import com.example.forcapp.entity.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


//TODO Eliminar strings estáticos e crealos comos recursos
public class FirebaseDAO {

    public final DatabaseReference databaseReference;
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
                partidaMap.put("isReadyPlayer1", partida.isReadyPlayer1());
                partidaMap.put("isReadyPlayer2", partida.isReadyPlayer2());
                partidaMap.put("isFinished", partida.isFinished());
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
                            if (snapshot.getValue().equals(true)) {
                                try {
                                    // Dar tempo para que o outro xogador saia
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                removeGame(partidaId);
                            }
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


    public Partida getGameById(String partidaId) throws InterruptedException {
        Partida partida;
        Thread.sleep(100);
        Task<DataSnapshot> task = databaseReference.child("Partida").child(partidaId).get();
        Thread.sleep(100);
        DataSnapshot dataSnapshot = task.getResult();
        Thread.sleep(100);
        if (dataSnapshot.exists()) {
            partida = dataSnapshot.getValue(Partida.class);
            Thread.sleep(100);
            return partida;
        }else
            return null;
    }


    public void updateGame(String partidaId, Partida partida) {
        Map<String, Object> partidaMap;

        partidaMap = new HashMap<>();
        partidaMap.put("player1", partida.getPlayer1());
        partidaMap.put("player2", partida.getPlayer2());
        partidaMap.put("randomWord", partida.getRandomWord());
        partidaMap.put("letrasAcertadas1", partida.getLetrasAcertadas1());
        partidaMap.put("letrasAcertadas2", partida.getLetrasAcertadas2());
        partidaMap.put("ganador", partida.getGanador());
        partidaMap.put("numPlayers", partida.getNumPlayers());
        partidaMap.put("isReadyPlayer1", partida.isReadyPlayer1());
        partidaMap.put("isReadyPlayer2", partida.isReadyPlayer2());
        partidaMap.put("isFinished", partida.isFinished());
        databaseReference.child("Partida").child(partidaId).updateChildren(partidaMap);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void player1Ready(String partidaId) {
        Map<String, Object> partidaMap;

        partidaMap = new HashMap<>();
        partidaMap.put("isReadyPlayer1", true);
        databaseReference.child("Partida").child(partidaId).updateChildren(partidaMap);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void player2Ready(String partidaId) {
        Map<String, Object> partidaMap;

        partidaMap = new HashMap<>();
        partidaMap.put("isReadyPlayer2", true);
        databaseReference.child("Partida").child(partidaId).updateChildren(partidaMap);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateWords1(String partidaId, String wordList) {
        class AddUser extends AsyncTask<Void, Void, Void> { // claseinterna
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> letrasMap;
                letrasMap = new HashMap<>();
                letrasMap.put("letrasAcertadas1", wordList);
                databaseReference.child("Partida").child(partidaId).updateChildren(letrasMap);
                return null;
            }

        }
        AddUser gf = new AddUser();
        gf.execute();
    }

    public void updateWords2(String partidaId, String wordList) {
        class AddUser extends AsyncTask<Void, Void, Void> { // claseinterna
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> letrasMap;
                letrasMap = new HashMap<>();
                letrasMap.put("letrasAcertadas2", wordList);
                databaseReference.child("Partida").child(partidaId).updateChildren(letrasMap);
                return null;
            }

        }
        AddUser gf = new AddUser();
        gf.execute();
    }

    public void setWinner(String partidaId, String email) {
        class AddUser extends AsyncTask<Void, Void, Void> { // claseinterna
            @Override
            protected Void doInBackground(Void... voids) {
                Map<String, Object> ganadorMap;
                ganadorMap = new HashMap<>();
                ganadorMap.put("ganador", email);
                databaseReference.child("Partida").child(partidaId).updateChildren(ganadorMap);
                return null;
            }

        }
        AddUser gf = new AddUser();
        gf.execute();
    }


}
