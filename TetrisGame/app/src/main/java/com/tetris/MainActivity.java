package com.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tetris.data.GameDataManager;

public class MainActivity extends AppCompatActivity {
    private GameDataManager dataManager;
    private TextView highScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = new GameDataManager(this);
        highScoreTextView = findViewById(R.id.high_score_text_view);

        // 显示最高分
        updateHighScore();

        Button startGameButton = findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开设置界面（如果需要）
                // startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHighScore();
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void updateHighScore() {
        int highScore = dataManager.getHighScore();
        highScoreTextView.setText(getString(R.string.high_score, highScore));
    }
}