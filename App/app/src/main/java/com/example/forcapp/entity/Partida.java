package com.example.forcapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Partida implements Parcelable {

    private boolean repeat1;
    private boolean repeat2;
    private String player1;
    private String player2;
    private String randomWord;
    private String letrasAcertadas1;
    private String letrasAcertadas2;
    private String ganador;
    private int numPlayers;
    private boolean isReadyPlayer1;
    private boolean isReadyPlayer2;
    private boolean isFinished;


    public Partida() {
    }

    public boolean isRepeat2() {
        return repeat2;
    }

    public void setRepeat2(boolean repeat2) {
        this.repeat2 = repeat2;
    }

    public boolean isRepeat1() {
        return repeat1;
    }

    public void setRepeat1(boolean repeat1) {
        this.repeat1 = repeat1;
    }

    public Partida(String player1, String randomWord, int numPlayers) {
        this.player1 = player1;
        this.player2 = "null";
        this.randomWord = randomWord;
        this.letrasAcertadas1 = "null";
        this.letrasAcertadas2 = "null";
        this.ganador = "null";
        this.numPlayers = numPlayers;
        this.isReadyPlayer1 = false;
        this.isReadyPlayer2 = false;
        this.isFinished = false;
        this.repeat1 = false;
        this.repeat2 = false;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getRandomWord() {
        return randomWord;
    }

    public void setRandomWord(String randomWord) {
        this.randomWord = randomWord;
    }

    public String getLetrasAcertadas1() {
        return letrasAcertadas1;
    }

    public void setLetrasAcertadas1(String letrasAcertadas1) {
        this.letrasAcertadas1 = letrasAcertadas1;
    }

    public String getLetrasAcertadas2() {
        return letrasAcertadas2;
    }

    public void setLetrasAcertadas2(String letrasAcertadas2) {
        this.letrasAcertadas2 = letrasAcertadas2;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public boolean isReadyPlayer1() {
        return isReadyPlayer1;
    }

    public void setReadyPlayer1(boolean readyPlayer1) {
        isReadyPlayer1 = readyPlayer1;
    }

    public boolean isReadyPlayer2() {
        return isReadyPlayer2;
    }

    public void setReadyPlayer2(boolean readyPlayer2) {
        isReadyPlayer2 = readyPlayer2;
    }

    public Partida(Parcel in) {
        String[] data = new String[12];

        in.readStringArray(data);
        this.player1 = data[0];
        this.player2 = data[1];
        this.randomWord = data[2];
        this.letrasAcertadas1 = data[3];
        this.letrasAcertadas2 = data[5];
        this.ganador = data[5];
        this.numPlayers = Integer.parseInt(data[6]);
        this.isReadyPlayer1 = Boolean.parseBoolean(data[7]);
        this.isReadyPlayer2 = Boolean.parseBoolean(data[8]);
        this.isFinished = Boolean.parseBoolean(data[9]);
        this.repeat1 = Boolean.parseBoolean(data[10]);
        this.repeat2 = Boolean.parseBoolean(data[11]);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.player1,
                this.player2,
                this.randomWord,
                this.letrasAcertadas1,
                this.letrasAcertadas2,
                this.ganador,
                String.valueOf(this.numPlayers),
                String.valueOf(this.isReadyPlayer1),
                String.valueOf(this.isReadyPlayer2),
                String.valueOf(this.isFinished),
                String.valueOf(this.repeat1),
                String.valueOf(this.repeat2)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Partida createFromParcel(Parcel in) {
            return new Partida(in);
        }

        public Partida[] newArray(int size) {
            return new Partida[size];
        }
    };

}
