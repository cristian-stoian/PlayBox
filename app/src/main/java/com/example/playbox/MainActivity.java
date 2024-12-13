package com.example.playbox;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    int[] diamonds ={
            R.drawable.green,
            R.drawable.blue,
            R.drawable.yellow,
            R.drawable.red,
            R.drawable.purple,
            R.drawable.orange

    };
    int widthOfBlock, noOfBlocks = 8, widthOfScreen;
    ArrayList<ImageView> diamond = new ArrayList<>();
    int diamondToBeDragged, diamondToBeReplaced;
    int notDiamond = R.drawable.transparent;
    Handler mHandler;
    int interval = 100;
    TextView scoreResult;
    int score = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreResult = findViewById(R.id.score);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen = displayMetrics.widthPixels;
        int heightOfScreen = displayMetrics.heightPixels;
        widthOfBlock = widthOfScreen / noOfBlocks;
        createBoard();


        for (ImageView imageView : diamond){
            imageView.setOnTouchListener(new OnSwipeListener(this){

                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    diamondToBeDragged = imageView.getId();
                    diamondToBeReplaced = diamondToBeDragged - 1;
                    diamondInterchange();

                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    diamondToBeDragged = imageView.getId();
                    diamondToBeReplaced = diamondToBeDragged + 1;
                    diamondInterchange();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    diamondToBeDragged = imageView.getId();
                    diamondToBeReplaced = diamondToBeDragged - noOfBlocks;
                    diamondInterchange();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    diamondToBeDragged = imageView.getId();
                    diamondToBeReplaced = diamondToBeDragged + noOfBlocks;
                    diamondInterchange();
                }
            });
        }

        mHandler = new Handler();
        startRepeat();
    }



    private void checkRowForThree(){
        for (int i=0; i<62;i++){
            int chosedDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = (int) diamond.get(i).getTag() == notDiamond;
            Integer[] notValid = {6,7,14,15,22,23,30,31,38,39,46,47,54,55};
            List<Integer> list = Arrays.asList(notValid);
            if (!list.contains(i))
            {
                int x = i;
                if ((int) diamond.get(x++).getTag() == chosedDiamond && !isBlank &&
                        (int) diamond.get(x++).getTag() == chosedDiamond &&
                        (int) diamond.get(x).getTag() == chosedDiamond)
                {
                    score = score +3;
                    scoreResult.setText(String.valueOf(score));
                    diamond.get(x).setImageResource(notDiamond);
                    diamond.get(x).setTag(notDiamond);
                    x--;
                    diamond.get(x).setImageResource(notDiamond);
                    diamond.get(x).setTag(notDiamond);
                    x--;
                    diamond.get(x).setImageResource(notDiamond);
                    diamond.get(x).setTag(notDiamond);
                }
            }

        }
        moveDownDiamonds();
    }

    private void checkColumnForThree(){
        for (int i=0; i<47;i++){
            int chosedDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = (int) diamond.get(i).getTag() == notDiamond;

                int x = i;
                if ((int) diamond.get(x).getTag() == chosedDiamond && !isBlank &&
                        (int) diamond.get(x+noOfBlocks).getTag() == chosedDiamond &&
                        (int) diamond.get(x+2*noOfBlocks).getTag() == chosedDiamond)
                {
                    score = score +3;
                    scoreResult.setText(String.valueOf(score));
                    diamond.get(x).setImageResource(notDiamond);
                    diamond.get(x).setTag(notDiamond);
                    x = x+noOfBlocks;
                    diamond.get(x).setImageResource(notDiamond);
                    diamond.get(x).setTag(notDiamond);
                    x = x+noOfBlocks;

                    diamond.get(x).setImageResource(notDiamond);
                    diamond.get(x).setTag(notDiamond);
                }
            }
        moveDownDiamonds();
        }

        private void moveDownDiamonds() {
            Integer[] firstRow = {0, 1, 2, 3, 4, 5, 6, 7};
            List<Integer> list = Arrays.asList(firstRow);

            for (int i = 55; i >= 0; i--) {
                if ((int) diamond.get(i + noOfBlocks).getTag() == notDiamond) {
                    diamond.get(i + noOfBlocks).setImageResource((int) diamond.get(i).getTag());
                    diamond.get(i + noOfBlocks).setTag(diamond.get(i).getTag());
                    diamond.get(i).setImageResource(notDiamond);
                    diamond.get(i).setTag(notDiamond);

                    if (list.contains(i) && (int) diamond.get(i).getTag() == notDiamond){
                        int randomDiamond = (int) Math.floor(Math.random() * diamonds.length);
                        diamond.get(i).setImageResource(diamonds[randomDiamond]);
                        diamond.get(i).setTag(diamonds[randomDiamond]);
                    }
                }
            }
            for (int i = 0; i < 8 ; i++){
                if ((int) diamond.get(i).getTag() == notDiamond ){
                    int randomDiamond = (int) Math.floor(Math.random() * diamonds.length);
                    diamond.get(i).setImageResource(diamonds[randomDiamond]);
                    diamond.get(i).setTag(diamonds[randomDiamond]);
                }

            }
        }

    private void checkRowForFour() {
        for (int i = 0; i < 61; i++) { // Ajustăm limita pentru a evita ieșirea din listă
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;
            Integer[] notValid = {5, 6, 7, 13, 14, 15, 21, 22, 23, 29, 30, 31, 37, 38, 39, 45, 46, 47, 53, 54, 55};
            List<Integer> list = Arrays.asList(notValid);

            if (!list.contains(i)) {
                int x = i;

                // Verificare potrivire de 4
                if ((int) diamond.get(x++).getTag() == chosenDiamond && !isBlank &&
                        (int) diamond.get(x++).getTag() == chosenDiamond &&
                        (int) diamond.get(x++).getTag() == chosenDiamond &&
                        (int) diamond.get(x).getTag() == chosenDiamond) {

                    score += 4; // Adăugăm punctaj pentru 4
                    scoreResult.setText(String.valueOf(score));

                    // Eliminăm cele 4 diamante
                    for (int j = 0; j < 4; j++) {
                        diamond.get(x--).setImageResource(notDiamond);
                        diamond.get(x + 1).setTag(notDiamond);
                    }
                }
                // Verificare potrivire de 3 (fallback)
                else if ((int) diamond.get(x++).getTag() == chosenDiamond && !isBlank &&
                        (int) diamond.get(x++).getTag() == chosenDiamond &&
                        (int) diamond.get(x).getTag() == chosenDiamond) {

                    score += 3;
                    scoreResult.setText(String.valueOf(score));

                    for (int j = 0; j < 3; j++) {
                        diamond.get(x--).setImageResource(notDiamond);
                        diamond.get(x + 1).setTag(notDiamond);
                    }
                }
            }
        }
        moveDownDiamonds();
    }

    private void checkColumnForFour() {
        for (int i = 0; i < 40; i++) { // Ajustăm limita pentru a evita ieșirea din listă
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;

            if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                    (int) diamond.get(i + noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 2 * noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 3 * noOfBlocks).getTag() == chosenDiamond) {

                score += 4; // Adăugăm punctaj pentru 4
                scoreResult.setText(String.valueOf(score));

                for (int j = 0; j < 4; j++) {
                    diamond.get(i + j * noOfBlocks).setImageResource(notDiamond);
                    diamond.get(i + j * noOfBlocks).setTag(notDiamond);
                }
            }
            // Verificare potrivire de 3 (fallback)
            else if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                    (int) diamond.get(i + noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 2 * noOfBlocks).getTag() == chosenDiamond) {

                score += 3;
                scoreResult.setText(String.valueOf(score));

                for (int j = 0; j < 3; j++) {
                    diamond.get(i + j * noOfBlocks).setImageResource(notDiamond);
                    diamond.get(i + j * noOfBlocks).setTag(notDiamond);
                }
            }
        }
        moveDownDiamonds();
    }
    private void checkRowForMatches() {
        for (int i = 0; i < 61; i++) { // Ajustăm limita pentru a evita ieșirea din listă
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;
            Integer[] notValid = {5, 6, 7, 13, 14, 15, 21, 22, 23, 29, 30, 31, 37, 38, 39, 45, 46, 47, 53, 54, 55};
            List<Integer> list = Arrays.asList(notValid);

            if (!list.contains(i)) {
                int x = i;

                // Verificare potrivire de 4
                if ((int) diamond.get(x++).getTag() == chosenDiamond && !isBlank &&
                        (int) diamond.get(x++).getTag() == chosenDiamond &&
                        (int) diamond.get(x++).getTag() == chosenDiamond &&
                        (int) diamond.get(x).getTag() == chosenDiamond) {

                    score += 4; // Adăugăm punctaj pentru 4
                    scoreResult.setText(String.valueOf(score));

                    // Eliminăm cele 4 diamante
                    for (int j = 0; j < 4; j++) {
                        diamond.get(x--).setImageResource(notDiamond);
                        diamond.get(x + 1).setTag(notDiamond);
                    }
                }
                // Verificare potrivire de 3 (fallback)
                else if ((int) diamond.get(x++).getTag() == chosenDiamond && !isBlank &&
                        (int) diamond.get(x++).getTag() == chosenDiamond &&
                        (int) diamond.get(x).getTag() == chosenDiamond) {

                    score += 3;
                    scoreResult.setText(String.valueOf(score));

                    for (int j = 0; j < 3; j++) {
                        diamond.get(x--).setImageResource(notDiamond);
                        diamond.get(x + 1).setTag(notDiamond);
                    }
                }
            }
        }
        moveDownDiamonds();
    }

    private void checkColumnForMatches() {
        for (int i = 0; i < 40; i++) { // Ajustăm limita pentru a evita ieșirea din listă
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;

            if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                    (int) diamond.get(i + noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 2 * noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 3 * noOfBlocks).getTag() == chosenDiamond) {

                score += 4; // Adăugăm punctaj pentru 4
                scoreResult.setText(String.valueOf(score));

                for (int j = 0; j < 4; j++) {
                    diamond.get(i + j * noOfBlocks).setImageResource(notDiamond);
                    diamond.get(i + j * noOfBlocks).setTag(notDiamond);
                }
            }
            // Verificare potrivire de 3 (fallback)
            else if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                    (int) diamond.get(i + noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 2 * noOfBlocks).getTag() == chosenDiamond) {

                score += 3;
                scoreResult.setText(String.valueOf(score));

                for (int j = 0; j < 3; j++) {
                    diamond.get(i + j * noOfBlocks).setImageResource(notDiamond);
                    diamond.get(i + j * noOfBlocks).setTag(notDiamond);
                }
            }
        }
        moveDownDiamonds();
    }





    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForFour();
                checkColumnForFour();
                moveDownDiamonds();
            }
            finally {
                mHandler.postDelayed(repeatChecker, interval);
            }
        }
    };

    void startRepeat(){
        repeatChecker.run();
    }

    private void diamondInterchange(){
        int background =(int) diamond.get(diamondToBeReplaced).getTag();
        int background1 =(int) diamond.get(diamondToBeDragged).getTag();
        diamond.get(diamondToBeDragged).setImageResource(background);
        diamond.get(diamondToBeReplaced).setImageResource(background1);
        diamond.get(diamondToBeDragged).setTag(background);
        diamond.get(diamondToBeReplaced).setTag(background1);
        Log.d("IndexDebug", "Dragged index: " + diamondToBeDragged + ", Replaced index: " + diamondToBeReplaced);

    }

    private void createBoard() {
        GridLayout gridLayout = findViewById(R.id.board);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.setColumnCount(noOfBlocks);
        gridLayout.getLayoutParams().width = widthOfScreen;
        gridLayout.getLayoutParams().height = widthOfScreen;
        for (int i = 0; i < noOfBlocks * noOfBlocks; i++){
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new
                    ViewGroup.LayoutParams(widthOfBlock, widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);
            int randomDiamond = (int) Math.floor(Math.random() * diamonds.length); //generate random value from diamond array
            imageView.setImageResource(diamonds[randomDiamond]);
            imageView.setTag(diamonds[randomDiamond]);
            diamond.add(imageView);
            gridLayout.addView(imageView);

        }
    }
    private void animateDiamondMovement(View diamond, float startX, float startY, float endX, float endY) {
        // Creează animația pe axa X
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(diamond, "x", startX, endX);
        animatorX.setDuration(300); // Durata animației, în milisecunde

        // Creează animația pe axa Y
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(diamond, "y", startY, endY);
        animatorY.setDuration(300);

        // Rulează animațiile împreună
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.start();
    }

}