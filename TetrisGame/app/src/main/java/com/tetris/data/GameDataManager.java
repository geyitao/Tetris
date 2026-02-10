package com.tetris.data;

import android.content.Context;
import android.content.SharedPreferences;

public class GameDataManager {
    private static final String PREF_NAME = "tetris_game_data";
    private static final String KEY_HIGH_SCORE = "high_score";
    private static final String KEY_GAME_COUNT = "game_count";
    private static final String KEY_TOTAL_LINES = "total_lines";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_DIFFICULTY = "difficulty";

    private SharedPreferences preferences;

    public GameDataManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getHighScore() {
        return preferences.getInt(KEY_HIGH_SCORE, 0);
    }

    public void setHighScore(int score) {
        int currentHighScore = getHighScore();
        if (score > currentHighScore) {
            preferences.edit().putInt(KEY_HIGH_SCORE, score).apply();
        }
    }

    public int getGameCount() {
        return preferences.getInt(KEY_GAME_COUNT, 0);
    }

    public void incrementGameCount() {
        int count = getGameCount();
        preferences.edit().putInt(KEY_GAME_COUNT, count + 1).apply();
    }

    public int getTotalLines() {
        return preferences.getInt(KEY_TOTAL_LINES, 0);
    }

    public void addLines(int lines) {
        int total = getTotalLines();
        preferences.edit().putInt(KEY_TOTAL_LINES, total + lines).apply();
    }

    public boolean isSoundEnabled() {
        return preferences.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public Difficulty getDifficulty() {
        String difficulty = preferences.getString(KEY_DIFFICULTY, Difficulty.NORMAL.name());
        return Difficulty.valueOf(difficulty);
    }

    public void setDifficulty(Difficulty difficulty) {
        preferences.edit().putString(KEY_DIFFICULTY, difficulty.name()).apply();
    }

    public enum Difficulty {
        EASY,
        NORMAL,
        HARD
    }
}