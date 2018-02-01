package com.yunke.xiaovo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

/**
 *
 */
public class BaseButton extends android.support.v7.widget.AppCompatButton {

    private Paint strokePaint;
    private int storkColor;

    public BaseButton(Context context) {
        this(context,null);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        strokePaint = getPaint();
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downAnimation();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            upAnimation();
        }

        return super.onTouchEvent(event);
    }


    private void downAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.9f, 1f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f  );
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        this.startAnimation(scaleAnimation);
    }


    private void upAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        this.startAnimation(scaleAnimation);
    }

}
