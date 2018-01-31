package com.yunke.xiaovo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

/**
 *
 */
public class BaseButton extends Button {
    public BaseButton(Context context) {
        super(context);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.1f, 1f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f  );
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        this.startAnimation(scaleAnimation);
    }


    private void upAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setFillAfter(true);
        this.startAnimation(scaleAnimation);
    }

}
