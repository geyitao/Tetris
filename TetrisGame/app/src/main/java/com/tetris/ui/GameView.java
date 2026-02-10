package com.tetris.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tetris.game.GameGrid;
import com.tetris.game.Tetromino;
import com.tetris.game.TetrisGame;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int BLOCK_SIZE = 30; // 方块大小（像素）
    private static final int GRID_WIDTH = GameGrid.WIDTH * BLOCK_SIZE;
    private static final int GRID_HEIGHT = GameGrid.HEIGHT * BLOCK_SIZE;

    private TetrisGame game;
    private Paint paint;
    private Paint gridPaint;
    private boolean isRunning;
    private GameThread gameThread;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(1);

        setFocusable(true);
    }

    public void setGame(TetrisGame game) {
        this.game = game;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        gameThread = new GameThread(getHolder());
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 调整视图大小以适应游戏区域
        getHolder().setFixedSize(GRID_WIDTH, GRID_HEIGHT);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void draw(Canvas canvas) {
        if (canvas == null || game == null) {
            return;
        }

        // 绘制背景
        canvas.drawColor(Color.BLACK);

        // 绘制网格线
        drawGrid(canvas);

        // 绘制已固定的方块
        drawGridBlocks(canvas);

        // 绘制当前移动的方块
        drawCurrentTetromino(canvas);

        // 如果游戏暂停，绘制暂停提示
        if (game.isPaused()) {
            drawPausedText(canvas);
        }

        // 如果游戏结束，绘制游戏结束提示
        if (game.isGameOver()) {
            drawGameOverText(canvas);
        }
    }

    private void drawGrid(Canvas canvas) {
        // 绘制垂直线
        for (int x = 0; x <= GRID_WIDTH; x += BLOCK_SIZE) {
            canvas.drawLine(x, 0, x, GRID_HEIGHT, gridPaint);
        }

        // 绘制水平线
        for (int y = 0; y <= GRID_HEIGHT; y += BLOCK_SIZE) {
            canvas.drawLine(0, y, GRID_WIDTH, y, gridPaint);
        }
    }

    private void drawGridBlocks(Canvas canvas) {
        GameGrid grid = game.getGrid();
        for (int y = 0; y < GameGrid.HEIGHT; y++) {
            for (int x = 0; x < GameGrid.WIDTH; x++) {
                int color = grid.getCellColor(x, y);
                if (color != Color.BLACK) {
                    drawBlock(canvas, x * BLOCK_SIZE, y * BLOCK_SIZE, color);
                }
            }
        }
    }

    private void drawCurrentTetromino(Canvas canvas) {
        Tetromino tetromino = game.getCurrentTetromino();
        if (tetromino == null) {
            return;
        }

        int[][] matrix = tetromino.getMatrix();
        int size = matrix.length;
        int x = tetromino.getX() * BLOCK_SIZE;
        int y = tetromino.getY() * BLOCK_SIZE;
        int color = tetromino.getColor();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != 0) {
                    drawBlock(canvas, x + j * BLOCK_SIZE, y + i * BLOCK_SIZE, color);
                }
            }
        }
    }

    private void drawBlock(Canvas canvas, int x, int y, int color) {
        paint.setColor(color);
        canvas.drawRect(x + 1, y + 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1, paint);

        // 绘制方块边框
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawRect(x + 1, y + 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawPausedText(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);

        String text = "暂停";
        float x = GRID_WIDTH / 2f;
        float y = GRID_HEIGHT / 2f;

        canvas.drawText(text, x, y, paint);
    }

    private void drawGameOverText(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);

        String text = "游戏结束";
        float x = GRID_WIDTH / 2f;
        float y = GRID_HEIGHT / 2f;

        canvas.drawText(text, x, y, paint);
    }

    public void update() {
        if (game != null && !game.isPaused() && !game.isGameOver()) {
            game.update();
        }
    }

    public void startGame() {
        if (game != null) {
            game.start();
        }
    }

    public void pauseGame() {
        if (game != null) {
            game.pause();
        }
    }

    public void resumeGame() {
        if (game != null) {
            game.resume();
        }
    }

    public void resetGame() {
        if (game != null) {
            game.reset();
        }
    }

    private class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;

        public GameThread(SurfaceHolder holder) {
            surfaceHolder = holder;
        }

        @Override
        public void run() {
            while (isRunning) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        update();
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

                // 控制游戏速度
                try {
                    Thread.sleep(game != null ? game.getDropSpeed() : 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}