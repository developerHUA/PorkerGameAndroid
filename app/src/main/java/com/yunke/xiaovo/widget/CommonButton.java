package com.yunke.xiaovo.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 *
 */
public class CommonButton extends android.support.v7.widget.AppCompatButton {

    private Paint strokePaint;
    private int storkColor;

    public CommonButton(Context context) {
        this(context,null);
    }

    public CommonButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        strokePaint = getPaint();
    }


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        clearAnimation();
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
