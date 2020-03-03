package com.example.myfitnessbuddy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    private float initialXValue;
    private SwipeDirection direction;

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.direction = SwipeDirection.all;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.IsSwipeAllowed(ev)){
            return super.onTouchEvent(ev);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.IsSwipeAllowed(ev)){
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent ev) {
        if(this.direction == SwipeDirection.all) return true;

        if(direction == SwipeDirection.none )//disable any swipe
            return false;

        if(ev.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = ev.getX();
            return true;
        }

        if(ev.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffX = ev.getX() - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.right ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 0 && direction == SwipeDirection.left ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }

}
