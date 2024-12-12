package com.example.playbox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    Toast.makeText(MainActivity.this, "Top", Toast.LENGTH_SHORT).show();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    Toast.makeText(MainActivity.this, "Bottom", Toast.LENGTH_SHORT).show();
                }
            });
        }







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
            diamond.add(imageView);
            gridLayout.addView(imageView);

        }



    }
}