package com.example.playbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ClickManiaActivity extends AppCompatActivity {

    private TextView timeTextView;
    private TextView clickCountTextView;
    private ImageView clickCircle;
    private RelativeLayout rootLayout;
    private Button homeButton, restartButton;

    private int clickCount = 0;
    private CountDownTimer timer;
    private long timeLeftInMillis = 10000;
    private Random random = new Random();
    private boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_mania);

        timeTextView = findViewById(R.id.timeTextView);
        clickCountTextView = findViewById(R.id.clickCountTextView);
        clickCircle = findViewById(R.id.clickCircle);
        rootLayout = findViewById(R.id.rootLayout);
        homeButton = findViewById(R.id.homeButton);
        restartButton = findViewById(R.id.restartButton);

        // Setează dimensiunea cercului mai mică
        int newSize = 100; // în pixeli
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(newSize, newSize);
        clickCircle.setLayoutParams(params);

        homeButton.setOnClickListener(v -> finish());

        restartButton.setOnClickListener(v -> resetGame());

        clickCircle.setOnClickListener(v -> {
            if (!gameStarted) {
                gameStarted = true;
                startTimer();
            }
            incrementClickCountAndMoveCircle();
        });

        // Mută cercul la o locație aleatorie inițială
        rootLayout.post(this::moveCircleRandomly);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimeText();
            }

            @Override
            public void onFinish() {
                timeTextView.setText("0");
                clickCircle.setEnabled(false);
                showEndGameDialog();
            }
        }.start();
    }

    private void updateTimeText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timeTextView.setText(String.valueOf(seconds));
    }

    private void incrementClickCountAndMoveCircle() {
        clickCount++;
        clickCountTextView.setText("Clicks: " + clickCount);

        // Micșorează cercul cu 1 pixel (în dp)
        int width = clickCircle.getWidth();
        int height = clickCircle.getHeight();

        int newWidth = width + 12;
        int newHeight = height + 12;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) clickCircle.getLayoutParams();
        layoutParams.width = newWidth;
        layoutParams.height = newHeight;
        clickCircle.setLayoutParams(layoutParams);

        // Mută cercul cu animație
        clickCircle.post(this::moveCircleRandomly);
    }

    private void moveCircleRandomly() {
        int layoutWidth = rootLayout.getWidth();
        int layoutHeight = rootLayout.getHeight();
        int circleWidth = clickCircle.getWidth();
        int circleHeight = clickCircle.getHeight();

        if (layoutWidth > 0 && layoutHeight > 0 && circleWidth > 0 && circleHeight > 0) {
            int maxX = layoutWidth - circleWidth;
            int maxY = layoutHeight - circleHeight;
            int newX = random.nextInt(maxX + 1);
            int newY = random.nextInt(maxY + 1);

            clickCircle.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(150)
                    .start();
        }
    }

    private void showEndGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Asigură-te că butoanele devin vizibile
        findViewById(R.id.endButtonsLayout).setVisibility(View.VISIBLE);
    }


    private void resetGame() {
        clickCount = 0;
        clickCountTextView.setText("Clicks: 0");
        timeLeftInMillis = 10000;
        updateTimeText();
        clickCircle.setEnabled(true);
        gameStarted = false;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) clickCircle.getLayoutParams();
        layoutParams.width = 100;
        layoutParams.height = 100;

        // Ascunde butoanele
        findViewById(R.id.endButtonsLayout).setVisibility(View.GONE);

        rootLayout.post(this::moveCircleRandomly);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
