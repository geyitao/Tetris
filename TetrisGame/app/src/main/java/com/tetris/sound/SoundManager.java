package com.tetris.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.tetris.R;

public class SoundManager {
    private Context context;
    private boolean isEnabled;
    private MediaPlayer moveSound;
    private MediaPlayer rotateSound;
    private MediaPlayer clearSound;
    private MediaPlayer gameOverSound;

    public SoundManager(Context context) {
        this.context = context;
        this.isEnabled = true;
        initSounds();
    }

    private void initSounds() {
        // 初始化各种音效
        moveSound = MediaPlayer.create(context, R.raw.move);
        rotateSound = MediaPlayer.create(context, R.raw.rotate);
        clearSound = MediaPlayer.create(context, R.raw.clear);
        gameOverSound = MediaPlayer.create(context, R.raw.game_over);
    }

    public void playMoveSound() {
        if (isEnabled && moveSound != null) {
            playSound(moveSound);
        }
    }

    public void playRotateSound() {
        if (isEnabled && rotateSound != null) {
            playSound(rotateSound);
        }
    }

    public void playClearSound() {
        if (isEnabled && clearSound != null) {
            playSound(clearSound);
        }
    }

    public void playGameOverSound() {
        if (isEnabled && gameOverSound != null) {
            playSound(gameOverSound);
        }
    }

    private void playSound(MediaPlayer player) {
        if (player.isPlaying()) {
            player.stop();
            player.reset();
            initSounds(); // 重新初始化音效
        }
        player.start();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void release() {
        // 释放所有音效资源
        if (moveSound != null) {
            moveSound.release();
            moveSound = null;
        }
        if (rotateSound != null) {
            rotateSound.release();
            rotateSound = null;
        }
        if (clearSound != null) {
            clearSound.release();
            clearSound = null;
        }
        if (gameOverSound != null) {
            gameOverSound.release();
            gameOverSound = null;
        }
    }
}