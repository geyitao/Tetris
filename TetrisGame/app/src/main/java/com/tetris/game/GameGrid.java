package com.tetris.game;

import android.graphics.Color;

public class GameGrid {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    private int[][] grid;

    public GameGrid() {
        grid = new int[HEIGHT][WIDTH];
        clear();
    }

    public void clear() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                grid[i][j] = 0; // 0 表示空单元格
            }
        }
    }

    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return false; // 边界外视为非空
        }
        return grid[y][x] == 0;
    }

    public boolean isValidMove(Tetromino tetromino, int newX, int newY) {
        int[][] matrix = tetromino.getMatrix();
        int size = matrix.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != 0) {
                    int x = newX + j;
                    int y = newY + i;

                    if (!isEmpty(x, y)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void placeTetromino(Tetromino tetromino) {
        int[][] matrix = tetromino.getMatrix();
        int size = matrix.length;
        int x = tetromino.getX();
        int y = tetromino.getY();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != 0) {
                    int gridX = x + j;
                    int gridY = y + i;

                    if (gridX >= 0 && gridX < WIDTH && gridY >= 0 && gridY < HEIGHT) {
                        grid[gridY][gridX] = matrix[i][j];
                    }
                }
            }
        }
    }

    public int clearCompletedLines() {
        int linesCleared = 0;

        for (int i = HEIGHT - 1; i >= 0; i--) {
            boolean isLineComplete = true;

            for (int j = 0; j < WIDTH; j++) {
                if (grid[i][j] == 0) {
                    isLineComplete = false;
                    break;
                }
            }

            if (isLineComplete) {
                // 将上面的行下移
                for (int k = i; k > 0; k--) {
                    System.arraycopy(grid[k - 1], 0, grid[k], 0, WIDTH);
                }
                // 顶部行清空
                for (int j = 0; j < WIDTH; j++) {
                    grid[0][j] = 0;
                }
                linesCleared++;
                i++; // 重新检查当前行（现在是原来的上一行）
            }
        }

        return linesCleared;
    }

    public boolean isGameOver() {
        // 检查顶部两行是否有方块
        for (int j = 0; j < WIDTH; j++) {
            if (grid[0][j] != 0 || grid[1][j] != 0) {
                return true;
            }
        }
        return false;
    }

    public int getCellColor(int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return Color.BLACK;
        }

        int cellValue = grid[y][x];
        switch (cellValue) {
            case 1: return Color.CYAN;        // I
            case 2: return Color.BLUE;        // J
            case 3: return Color.parseColor("#FFA500"); // L (Orange)
            case 4: return Color.YELLOW;      // O
            case 5: return Color.GREEN;       // S
            case 6: return Color.parseColor("#800080"); // T (Purple)
            case 7: return Color.RED;         // Z
            default: return Color.BLACK;      // 空
        }
    }

    public int[][] getGrid() {
        return grid;
    }
}