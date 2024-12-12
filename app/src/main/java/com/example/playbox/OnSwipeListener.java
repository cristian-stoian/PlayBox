package com.example.playbox;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.OnSwipe;

public class OnSwipeListener  implements View.OnTouchListener {
    public GestureDetector gestureDetector;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    public OnSwipeListener(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        public static final int  SWIPE_THREESOLD = 100;
        public static final int SWIPE_VELOCITY_THREESOLD = 100;

        @Override
        public boolean onDown( MotionEvent e) {
          return true;
        }

        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            float yDiff = e2.getY() - e1.getY();
            float xDiff = e2.getX() - e1.getX();
            if (Math.abs(xDiff) > Math.abs(yDiff)){
                //it means that we are either going to left or right direction
                if (Math.abs(xDiff) > SWIPE_THREESOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THREESOLD){
                    if (xDiff > 0){
                        onSwipeRight();
                    }else {
                        onSwipeLeft();
                    }
                    result = true;
                }
            }
            else if(Math.abs(yDiff) > SWIPE_THREESOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THREESOLD){
                if (yDiff > 0){
                    onSwipeBottom();
                }else {
                    onSwipeTop();
                }
                result = true;
            }
            return result;
        }
    }
    void onSwipeLeft(){}
    void onSwipeRight(){}
    void onSwipeTop(){}
    void onSwipeBottom(){}
}

