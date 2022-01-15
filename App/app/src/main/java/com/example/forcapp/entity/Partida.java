package com.example.forcapp.entity;

public class Partida {

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

    public Partida(String player1, String randomWord, int numPlayers ) {
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
}