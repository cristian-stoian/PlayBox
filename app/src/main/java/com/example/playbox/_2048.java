package com.example.playbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class _2048 extends AppCompatActivity {

    private TextView[][] gameBoard = new TextView[4][4];
    private int[][] board = new int[4][4];
    private int score = 0;
    private TextView scoreTextView;
    private Button homeButton; // Adaugă o variabilă pentru butonul Home

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._2048);

        scoreTextView = findViewById(R.id.scoreText);
        homeButton = findViewById(R.id.homeButton); // Initializează butonul Home

        // Setează un OnClickListener pentru butonul Home
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creează un Intent pentru a porni MainActivity
                Intent intent = new Intent(_2048.this, MainActivity.class);
                startActivity(intent);
                finish(); // Închide GameActivity pentru a nu se mai întoarce la el cu butonul Back
            }
        });

        gameBoard[0][0] = findViewById(R.id.cell_00);
        gameBoard[0][1] = findViewById(R.id.cell_01);
        gameBoard[0][2] = findViewById(R.id.cell_02);
        gameBoard[0][3] = findViewById(R.id.cell_03);
        gameBoard[1][0] = findViewById(R.id.cell_10);
        gameBoard[1][1] = findViewById(R.id.cell_11);
        gameBoard[1][2] = findViewById(R.id.cell_12);
        gameBoard[1][3] = findViewById(R.id.cell_13);
        gameBoard[2][0] = findViewById(R.id.cell_20);
        gameBoard[2][1] = findViewById(R.id.cell_21);
        gameBoard[2][2] = findViewById(R.id.cell_22);
        gameBoard[2][3] = findViewById(R.id.cell_23);
        gameBoard[3][0] = findViewById(R.id.cell_30);
        gameBoard[3][1] = findViewById(R.id.cell_31);
        gameBoard[3][2] = findViewById(R.id.cell_32);
        gameBoard[3][3] = findViewById(R.id.cell_33);

        initializeGame();

        // Detectare swipe (restul codului tău pentru swipe rămâne neschimbat)
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                boolean moved = false;

                if (Math.abs(dx) > Math.abs(dy)) {
                    if (Math.abs(dx) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (dx > 0) moved = swipeRight(); else moved = swipeLeft();
                        if (moved) addNewTile();
                        updateBoard();
                        if (isGameOver()) {
                            showGameOverDialog();
                        }
                        return true;
                    }
                } else {
                    if (Math.abs(dy) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (dy > 0) moved = swipeDown(); else moved = swipeUp();
                        if (moved) addNewTile();
                        updateBoard();
                        if (isGameOver()) {
                            showGameOverDialog();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initializeGame() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                board[i][j] = 0;
        score = 0;
        updateScore();
        addNewTile();
        addNewTile();
        updateBoard();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // Actualizare UI
    private void updateBoard() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                int value = board[i][j];
                gameBoard[i][j].setText(value == 0 ? "" : String.valueOf(value));
                setCellColor(gameBoard[i][j], value);
            }
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    private boolean hasEmptyCell() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (board[i][j] == 0)
                    return true;
        return false;
    }

    private void addNewTile() {
        if (!hasEmptyCell()) return;

        Random rand = new Random();
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int[] randomCell = emptyCells.get(rand.nextInt(emptyCells.size()));
            board[randomCell[0]][randomCell[1]] = rand.nextInt(10) < 9 ? 2 : 4;
        }
    }

    private boolean swipeLeft() {
        boolean moved = false;

        for (int i = 0; i < 4; i++) {
            int[] row = board[i];
            boolean[] merged = new boolean[4];
            int[] newRow = new int[4];
            int index = 0;

            for (int j = 0; j < 4; j++) {
                if (row[j] != 0) {
                    if (newRow[index] == 0) {
                        newRow[index] = row[j];
                    } else if (newRow[index] == row[j] && !merged[index]) {
                        newRow[index] *= 2;
                        score += newRow[index];
                        merged[index] = true;
                        moved = true;
                    } else {
                        index++;
                        newRow[index] = row[j];
                    }
                }
            }

            for (int j = 0; j < 4; j++) {
                if (board[i][j] != newRow[j]) {
                    moved = true;
                    board[i][j] = newRow[j];
                }
            }
        }
        if (moved) updateScore();
        return moved;
    }

    private boolean swipeRight() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            int[] row = board[i];
            boolean[] merged = new boolean[4];
            int[] newRow = new int[4];
            int index = 3;

            for (int j = 3; j >= 0; j--) {
                if (row[j] != 0) {
                    if (newRow[index] == 0) {
                        newRow[index] = row[j];
                    } else if (newRow[index] == row[j] && !merged[index]) {
                        newRow[index] *= 2;
                        score += newRow[index];
                        merged[index] = true;
                        moved = true;
                    } else {
                        index--;
                        newRow[index] = row[j];
                    }
                }
            }

            for (int j = 0; j < 4; j++) {
                if (board[i][j] != newRow[j]) {
                    moved = true;
                    board[i][j] = newRow[j];
                }
            }
        }
        if (moved) updateScore();
        return moved;
    }

    private boolean swipeUp() {
        transpose();
        boolean moved = swipeLeft();
        transpose();
        return moved;
    }

    private boolean swipeDown() {
        transpose();
        boolean moved = swipeRightLogic();
        transpose();
        return moved;
    }

    private boolean swipeRightLogic() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            int[] row = board[i];
            boolean[] merged = new boolean[4];
            int[] newRow = new int[4];
            int index = 3;

            for (int j = 3; j >= 0; j--) {
                if (row[j] != 0) {
                    if (newRow[index] == 0) {
                        newRow[index] = row[j];
                    } else if (newRow[index] == row[j] && !merged[index]) {
                        newRow[index] *= 2;
                        score += newRow[index];
                        merged[index] = true;
                        moved = true;
                    } else {
                        index--;
                        newRow[index] = row[j];
                    }
                }
            }

            for (int j = 0; j < 4; j++) {
                if (board[i][j] != newRow[j]) {
                    moved = true;
                    board[i][j] = newRow[j];
                }
            }
        }
        if (moved) updateScore();
        return moved;
    }

    private void transpose() {
        for (int i = 0; i < 4; i++)
            for (int j = i + 1; j < 4; j++) {
                int temp = board[i][j];
                board[i][j] = board[j][i];
                board[j][i] = temp;
            }
    }

    private boolean canMove() {
        // Verifică dacă există celule goale
        if (hasEmptyCell()) {
            return true;
        }

        // Verifică dacă există celule adiacente cu aceeași valoare
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((i > 0 && board[i][j] == board[i - 1][j]) ||
                        (i < 3 && board[i][j] == board[i + 1][j]) ||
                        (j > 0 && board[i][j] == board[i][j - 1]) ||
                        (j < 3 && board[i][j] == board[i][j + 1])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGameOver() {
        return !canMove();
    }

    private void setCellColor(TextView cell, int value) {
        switch (value) {
            case 2:
                cell.setBackgroundColor(Color.parseColor("#eee4da"));
                cell.setTextColor(Color.parseColor("#776e65"));
                break;
            case 4:
                cell.setBackgroundColor(Color.parseColor("#ede0c8"));
                cell.setTextColor(Color.parseColor("#776e65"));
                break;
            case 8:
                cell.setBackgroundColor(Color.parseColor("#f2b179"));
                cell.setTextColor(Color.WHITE);
                break;
            case 16:
                cell.setBackgroundColor(Color.parseColor("#f59563"));
                cell.setTextColor(Color.WHITE);
                break;
            case 32:
                cell.setBackgroundColor(Color.parseColor("#f67c5f"));
                cell.setTextColor(Color.WHITE);
                break;
            case 64:
                cell.setBackgroundColor(Color.parseColor("#f65e3b"));
                cell.setTextColor(Color.WHITE);
                break;
            case 128:
                cell.setBackgroundColor(Color.parseColor("#edcf72"));
                cell.setTextColor(Color.WHITE);
                break;
            case 256:
                cell.setBackgroundColor(Color.parseColor("#edcc61"));
                cell.setTextColor(Color.WHITE);
                break;
            case 512:
                cell.setBackgroundColor(Color.parseColor("#edc850"));
                cell.setTextColor(Color.WHITE);
                break;
            case 1024:
                cell.setBackgroundColor(Color.parseColor("#edc53f"));
                cell.setTextColor(Color.WHITE);
                break;
            case 2048:
                cell.setBackgroundColor(Color.parseColor("#edc22e"));
                cell.setTextColor(Color.WHITE);
                break;
            default:
                cell.setBackgroundColor(Color.parseColor("#cdc1b4"));
                cell.setTextColor(Color.parseColor("#776e65"));
                break;
        }
    }

    private void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over!")
                .setMessage("Scorul tău este: " + score)
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        initializeGame(); // Repornește jocul
                    }
                })
                .setNegativeButton("Închide", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // Închide activitatea
                    }
                })
                .setCancelable(false) // Utilizatorul nu poate închide dialogul dând click în afara lui
                .show();
    }
}