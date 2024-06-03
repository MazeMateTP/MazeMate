package com.example.tp_game;

public class GameData {
    private static GameData instance;
    private boolean isGameFinished;
    private boolean isGameStarted;
    private long gameStartTime;
    private long gameEndTime;

    private GameData() {
        // Private constructor to prevent instantiation
    }

    public static synchronized GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    // Getter and setter for isGameFinished
    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
        if (gameFinished) {
            gameEndTime = System.currentTimeMillis();
            notifyGameFinished();
        }
    }

    // Getter and setter for isGameStarted
    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
        if (gameStarted) {
            gameStartTime = System.currentTimeMillis();
            notifyGameStarted();
        }
    }

    // Method to get the duration of the game
    public long getGameDuration() {
        if (isGameFinished && gameStartTime > 0 && gameEndTime > 0) {
            return gameEndTime - gameStartTime;
        } else {
            return 0; // 게임이 종료되지 않았거나 시작 시간이 설정되지 않은 경우
        }
    }

    public interface GameStatusListener {
        void onGameFinished();
        void onGameStarted();
    }

    private GameStatusListener listener;

    public void setGameStatusListener(GameStatusListener listener) {
        this.listener = listener;
    }

    private void notifyGameFinished() {
        if (listener != null) {
            listener.onGameFinished();
        }
    }

    private void notifyGameStarted() {
        if (listener != null) {
            listener.onGameStarted();
        }
    }
}
