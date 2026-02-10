package com.tetris.game;

import android.graphics.Color;

public class Tetromino {
    public enum Shape {
        I, J, L, O, S, T, Z
    }

    private Shape shape;
    private int[][] matrix;
    private int color;
    private int x, y;

    public Tetromino(Shape shape) {
        this.shape = shape;
        initMatrix();
        initColor();
        resetPosition();
    }

    private void initMatrix() {
        switch (shape) {
            case I:
                matrix = new int[][] {
                    {0, 0, 0, 0},
                    {1, 1, 1, 1},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
                };
                break;
            case J:
                matrix = new int[][] {
                    {2, 0, 0},
                    {2, 2, 2},
                    {0, 0, 0}
                };
                break;
            case L:
                matrix = new int[][] {
                    {0, 0, 3},
                    {3, 3, 3},
                    {0, 0, 0}
                };
                break;
            case O:
                matrix = new int[][] {
                    {4, 4},
                    {4, 4}
                };
                break;
            case S:
                matrix = new int[][] {
                    {0, 5, 5},
                    {5, 5, 0},
                    {0, 0, 0}
                };
                break;
            case T:
                matrix = new int[][] {
                    {0, 6, 0},
                    {6, 6, 6},
                    {0, 0, 0}
                };
                break;
            case Z:
                matrix = new int[][] {
                    {7, 7, 0},
                    {0, 7, 7},
                    {0, 0, 0}
                };
                break;
        }
    }

    private void initColor() {
        switch (shape) {
            case I:
                color = Color.CYAN;
                break;
            case J:
                color = Color.BLUE;
                break;
            case L:
                color = Color.parseColor("#FFA500"); // Orange
                break;
            case O:
                color = Color.YELLOW;
                break;
            case S:
                color = Color.GREEN;
                break;
            case T:
                color = Color.parseColor("#800080"); // Purple
                break;
            case Z:
                color = Color.RED;
                break;
            default:
                color = Color.WHITE;
                break;
        }
    }

    public void resetPosition() {
        this.x = 4; // 初始位置在游戏区域中间
        this.y = 0;
    }

    public void rotate() {
        int size = matrix.length;
        int[][] rotated = new int[size][size];

        // 顺时针旋转矩阵
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotated[j][size - 1 - i] = matrix[i][j];
            }
        }

        matrix = rotated;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void moveDown() {
        y++;
    }

    public void moveUp() {
        y--;
    }

    // Getters
    public Shape getShape() {
        return shape;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return matrix.length;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}