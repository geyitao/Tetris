package com.tetris.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tetris.game.Tetromino;

public class NextBlockView extends View {
    private static final int BLOCK_SIZE = 20; // 预览方块大小（像素）
    private Tetromino nextTetromino;
    private Paint paint;
    private Paint gridPaint;

    public NextBlockView(Context context) {
        super(context);
        init();
    }

    public NextBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NextBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint();
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(1);
    }

    public void setNextTetromino(Tetromino tetromino) {
        this.nextTetromino = tetromino;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景
        canvas.drawColor(Color.BLACK);

        // 绘制边框
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, gridPaint);

        if (nextTetromino == null) {
            return;
        }

        // 计算方块在预览区域的居中位置
        int[][] matrix = nextTetromino.getMatrix();
        int size = matrix.length;
        int blockWidth = size * BLOCK_SIZE;
        int blockHeight = size * BLOCK_SIZE;

        int offsetX = (getWidth() - blockWidth) / 2;
        int offsetY = (getHeight() - blockHeight) / 2;

        // 绘制方块
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != 0) {
                    int x = offsetX + j * BLOCK_SIZE;
                    int y = offsetY + i * BLOCK_SIZE;
                    drawBlock(canvas, x, y, nextTetromino.getColor());
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
}