package com.yunke.xiaovo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.utils.BitmapUtil;

/**
 *
 */
public class CommonButton extends android.support.v7.widget.AppCompatButton {

    private Paint strokePaint;
    private Bitmap imageBitmap;
    private int resourceId;

    public CommonButton(Context context) {
        this(context, null);
    }

    public CommonButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CommonButton);
        resourceId  = typedArray.getResourceId(R.styleable.CommonButton_textImage, 0);
        typedArray.recycle();
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
    }


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        clearAnimation();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        scaleImage();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (imageBitmap != null) {
            canvas.drawBitmap(imageBitmap, getMeasuredWidth() / 2 - imageBitmap.getWidth() / 2,
                    getMeasuredHeight() / 2 - imageBitmap.getHeight() / 2, strokePaint);

        }
    }


    private void scaleImage() {
        if (resourceId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
            if (bitmap.getWidth() > getMeasuredWidth() * 0.65f
                    || bitmap.getHeight() > getMeasuredHeight() * 0.5f) {
                float bitmapWidth = bitmap.getWidth();
                float bitmapHeight = bitmap.getHeight();
                float scaleWidth = getMeasuredWidth() * 0.65f / bitmapWidth;
                float scaleHeight = getMeasuredHeight() * 0.5f / bitmapHeight;
                imageBitmap = BitmapUtil.scaleBitmap(bitmap, scaleWidth, scaleHeight);
            }
        }

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
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
