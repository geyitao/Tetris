package com.tetris;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tetris.data.GameDataManager;
import com.tetris.game.TetrisGame;
import com.tetris.sound.SoundManager;
import com.tetris.ui.GameView;
import com.tetris.ui.NextBlockView;

public class GameActivity extends AppCompatActivity {
    private TetrisGame game;
    private GameView gameView;
    private NextBlockView nextBlockView;
    private TextView scoreTextView;
    private TextView levelTextView;
    private TextView linesTextView;
    private Button pauseButton;
    private Button restartButton;
    private SoundManager soundManager;
    private GameDataManager dataManager;
    private GestureDetector gestureDetector;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // 初始化游戏
        game = new TetrisGame();
        dataManager = new GameDataManager(this);
        soundManager = new SoundManager(this);

        // 设置音效状态
        soundManager.setEnabled(dataManager.isSoundEnabled());

        // 初始化视图
        gameView = findViewById(R.id.game_view);
        gameView.setGame(game);

        nextBlockView = findViewById(R.id.next_block_view);
        nextBlockView.setNextTetromino(game.getNextTetromino());

        scoreTextView = findViewById(R.id.score_text_view);
        levelTextView = findViewById(R.id.level_text_view);
        linesTextView = findViewById(R.id.lines_text_view);

        pauseButton = findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePause();
            }
        });

        restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        // 初始化手势检测器
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                // 向右滑动
                                game.moveRight();
                                soundManager.playMoveSound();
                            } else {
                                // 向左滑动
                                game.moveLeft();
                                soundManager.playMoveSound();
                            }
                            result = true;
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                // 向下滑动
                                game.dropDown();
                                soundManager.playMoveSound();
                            } else {
                                // 向上滑动
                                game.rotate();
                                soundManager.playRotateSound();
                            }
                            result = true;
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // 点击屏幕旋转方块
                game.rotate();
                soundManager.playRotateSound();
                return true;
            }
        });

        // 启动游戏
        gameView.startGame();

        // 开始更新UI的线程
        startUpdateThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private void startUpdateThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });

                    try {
                        Thread.sleep(100); // 每100毫秒更新一次UI
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void updateUI() {
        // 更新下一个方块预览
        nextBlockView.setNextTetromino(game.getNextTetromino());

        // 更新分数、等级和行数
        scoreTextView.setText(getString(R.string.score, game.getScore()));
        levelTextView.setText(getString(R.string.level, game.getLevel()));
        linesTextView.setText(getString(R.string.lines, game.getLinesCleared()));

        // 检查游戏是否结束
        if (game.isGameOver()) {
            handleGameOver();
        }
    }

    private void togglePause() {
        if (isPaused) {
            gameView.resumeGame();
            pauseButton.setText(R.string.pause);
        } else {
            gameView.pauseGame();
            pauseButton.setText(R.string.resume);
        }
        isPaused = !isPaused;
    }

    private void restartGame() {
        game.reset();
        gameView.resetGame();
        pauseButton.setText(R.string.pause);
        isPaused = false;
    }

    private void handleGameOver() {
        // 播放游戏结束音效
        soundManager.playGameOverSound();

        // 更新游戏数据
        dataManager.setHighScore(game.getScore());
        dataManager.incrementGameCount();
        dataManager.addLines(game.getLinesCleared());

        // 这里可以添加游戏结束的提示对话框
        // 例如使用AlertDialog显示最终分数和重新开始选项
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!game.isGameOver()) {
            gameView.pauseGame();
            isPaused = true;
            pauseButton.setText(R.string.resume);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.release();
    }
}