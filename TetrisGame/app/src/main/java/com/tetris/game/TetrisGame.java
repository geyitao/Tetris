package com.tetris.game;

import java.util.Random;

public class TetrisGame {
    private GameGrid grid;
    private Tetromino currentTetromino;
    private Tetromino nextTetromino;
    private int score;
    private int level;
    private int linesCleared;
    private boolean isPaused;
    private boolean isGameOver;
    private Random random;
    private int dropSpeed;

    public TetrisGame() {
        grid = new GameGrid();
        random = new Random();
        reset();
    }

    public void reset() {
        grid.clear();
        score = 0;
        level = 1;
        linesCleared = 0;
        isPaused = false;
        isGameOver = false;
        dropSpeed = 1000; // 初始下落速度（毫秒）

        currentTetromino = generateRandomTetromino();
        nextTetromino = generateRandomTetromino();
    }

    private Tetromino generateRandomTetromino() {
        Tetromino.Shape[] shapes = Tetromino.Shape.values();
        Tetromino.Shape randomShape = shapes[random.nextInt(shapes.length)];
        return new Tetromino(randomShape);
    }

    public void start() {
        isPaused = false;
        isGameOver = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void update() {
        if (isPaused || isGameOver) {
            return;
        }

        if (currentTetromino == null) {
            currentTetromino = nextTetromino;
            nextTetromino = generateRandomTetromino();

            // 检查游戏是否结束
            if (!grid.isValidMove(currentTetromino, currentTetromino.getX(), currentTetromino.getY())) {
                isGameOver = true;
                return;
            }
        }

        // 尝试下移方块
        if (grid.isValidMove(currentTetromino, currentTetromino.getX(), currentTetromino.getY() + 1)) {
            currentTetromino.moveDown();
        } else {
            // 方块无法下移，固定到网格上
            grid.placeTetromino(currentTetromino);

            // 检查并清除已完成的行
            int lines = grid.clearCompletedLines();
            if (lines > 0) {
                linesCleared += lines;
                updateScore(lines);
                updateLevel();
            }

            // 生成新方块
            currentTetromino = nextTetromino;
            nextTetromino = generateRandomTetromino();

            // 检查游戏是否结束
            if (!grid.isValidMove(currentTetromino, currentTetromino.getX(), currentTetromino.getY())) {
                isGameOver = true;
            }
        }
    }

    private void updateScore(int lines) {
        // 根据消除的行数计算分数
        switch (lines) {
            case 1:
                score += 100 * level;
                break;
            case 2:
                score += 300 * level;
                break;
            case 3:
                score += 500 * level;
                break;
            case 4: // 俄罗斯方块（Tetris）
                score += 800 * level;
                break;
        }
    }

    private void updateLevel() {
        // 每消除10行提升一个等级
        int newLevel = (linesCleared / 10) + 1;
        if (newLevel > level) {
            level = newLevel;
            // 随着等级提升，下落速度加快
            dropSpeed = Math.max(100, 1000 - (level - 1) * 100);
        }
    }

    public void moveLeft() {
        if (isPaused || isGameOver || currentTetromino == null) {
            return;
        }

        if (grid.isValidMove(currentTetromino, currentTetromino.getX() - 1, currentTetromino.getY())) {
            currentTetromino.moveLeft();
        }
    }

    public void moveRight() {
        if (isPaused || isGameOver || currentTetromino == null) {
            return;
        }

        if (grid.isValidMove(currentTetromino, currentTetromino.getX() + 1, currentTetromino.getY())) {
            currentTetromino.moveRight();
        }
    }

    public void rotate() {
        if (isPaused || isGameOver || currentTetromino == null) {
            return;
        }

        Tetromino rotated = new Tetromino(currentTetromino.getShape());
        rotated.setPosition(currentTetromino.getX(), currentTetromino.getY());
        rotated.rotate();

        if (grid.isValidMove(rotated, rotated.getX(), rotated.getY())) {
            currentTetromino.rotate();
        }
    }

    public void dropDown() {
        if (isPaused || isGameOver || currentTetromino == null) {
            return;
        }

        while (grid.isValidMove(currentTetromino, currentTetromino.getX(), currentTetromino.getY() + 1)) {
            currentTetromino.moveDown();
        }

        // 立即更新游戏状态
        update();
    }

    // Getters
    public GameGrid getGrid() {
        return grid;
    }

    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getDropSpeed() {
        return dropSpeed;
    }
}