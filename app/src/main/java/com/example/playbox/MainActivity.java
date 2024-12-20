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
    private void checkRowForFour() {
        for (int i = 0; i < 61; i++) { // Ultimele 3 poziții nu pot avea match de 4
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;

            Integer[] notValid = {5, 6, 7, 13, 14, 15, 21, 22, 23, 29, 30, 31, 37, 38, 39, 45, 46, 47, 53, 54, 55}; // Ultimele 3 poziții din fiecare rând
            List<Integer> list = Arrays.asList(notValid);

            if (!list.contains(i)) {
                if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                        (int) diamond.get(i + 1).getTag() == chosenDiamond &&
                        (int) diamond.get(i + 2).getTag() == chosenDiamond &&
                        (int) diamond.get(i + 3).getTag() == chosenDiamond) {

                    // Adaugă la scor
                    score += 4;
                    scoreResult.setText(String.valueOf(score));

                    // Elimină toate cele 4 diamante
                    for (int j = 0; j < 4; j++) {
                        int index = i + j;
                        diamond.get(index).setImageResource(notDiamond);
                        diamond.get(index).setTag(notDiamond);
                    }
                }
            }
        }
        moveDownDiamonds();
    }
    private void checkColumnForFour() {
        for (int i = 0; i < 40; i++) { // Ultimele 3 rânduri nu pot avea match de 4
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;

            if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                    (int) diamond.get(i + noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 2 * noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 3 * noOfBlocks).getTag() == chosenDiamond) {

                // Adaugă la scor
                score += 4;
                scoreResult.setText(String.valueOf(score));

                // Elimină diamantele
                for (int j = 0; j < 4; j++) {
                    int index = i + j * noOfBlocks;
                    diamond.get(index).setImageResource(notDiamond);
                    diamond.get(index).setTag(notDiamond);
                }
            }
        }
        moveDownDiamonds();
    }
    private void checkRowForFive() {
        for (int i = 0; i < 60; i++) { // Ultimele 4 poziții nu pot avea match de 5
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;

            Integer[] notValid = {4, 5, 6, 7, 12, 13, 14, 15, 20, 21, 22, 23, 28, 29, 30, 31, 36, 37, 38, 39, 44, 45, 46, 47, 52, 53, 54, 55}; // Ultimele 4 poziții din fiecare rând
            List<Integer> list = Arrays.asList(notValid);

            if (!list.contains(i)) {
                if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                        (int) diamond.get(i + 1).getTag() == chosenDiamond &&
                        (int) diamond.get(i + 2).getTag() == chosenDiamond &&
                        (int) diamond.get(i + 3).getTag() == chosenDiamond &&
                        (int) diamond.get(i + 4).getTag() == chosenDiamond) {

                    // Adaugă la scor
                    score += 5;
                    scoreResult.setText(String.valueOf(score));

                    // Elimină toate cele 5 diamante
                    for (int j = 0; j < 5; j++) {
                        int index = i + j;
                        diamond.get(index).setImageResource(notDiamond);
                        diamond.get(index).setTag(notDiamond);
                    }
                }
            }
        }
        moveDownDiamonds();
    }
    private void checkColumnForFive() {
        for (int i = 0; i < 32; i++) { // Ultimele 4 rânduri nu pot avea match de 5
            int chosenDiamond = (int) diamond.get(i).getTag();
            boolean isBlank = chosenDiamond == notDiamond;

            if ((int) diamond.get(i).getTag() == chosenDiamond && !isBlank &&
                    (int) diamond.get(i + noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 2 * noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 3 * noOfBlocks).getTag() == chosenDiamond &&
                    (int) diamond.get(i + 4 * noOfBlocks).getTag() == chosenDiamond) {

                // Adaugă la scor
                score += 5;
                scoreResult.setText(String.valueOf(score));

                // Elimină toate cele 5 diamante
                for (int j = 0; j < 5; j++) {
                    int index = i + j * noOfBlocks;
                    diamond.get(index).setImageResource(notDiamond);
                    diamond.get(index).setTag(notDiamond);
                }
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


    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForFive();
                checkColumnForFive();
                checkRowForFour();
                checkColumnForFour();
                checkRowForThree();
                checkColumnForThree();
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

    private void diamondInterchange() {
        // Salvează starea inițială
        int initialScore = score;
        int draggedBackground = (int) diamond.get(diamondToBeDragged).getTag();
        int replacedBackground = (int) diamond.get(diamondToBeReplaced).getTag();

        // Efectuează schimbul
        diamond.get(diamondToBeDragged).setImageResource(replacedBackground);
        diamond.get(diamondToBeReplaced).setImageResource(draggedBackground);
        diamond.get(diamondToBeDragged).setTag(replacedBackground);
        diamond.get(diamondToBeReplaced).setTag(draggedBackground);

        Log.d("Swap", "Diamonds swapped: Dragged=" + diamondToBeDragged + ", Replaced=" + diamondToBeReplaced);

        // Verifică dacă scorul s-a schimbat după un interval scurt
        new Handler().postDelayed(() -> {
            if (score == initialScore) {
                // Revert schimbul
                diamond.get(diamondToBeDragged).setImageResource(draggedBackground);
                diamond.get(diamondToBeReplaced).setImageResource(replacedBackground);
                diamond.get(diamondToBeDragged).setTag(draggedBackground);
                diamond.get(diamondToBeReplaced).setTag(replacedBackground);
                Log.d("SwapRevert", "Swap reverted due to no score change.");
            } else {
                Log.d("SwapSuccess", "Swap successful, score increased.");
            }
        }, 200); // Așteaptă 200ms pentru verificare
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
            int randomDiamond = (int) Math.floor(Math.random() * diamonds.length);
            imageView.setImageResource(diamonds[randomDiamond]);
            imageView.setTag(diamonds[randomDiamond]);
            diamond.add(imageView);
            gridLayout.addView(imageView);

        }
    }
}