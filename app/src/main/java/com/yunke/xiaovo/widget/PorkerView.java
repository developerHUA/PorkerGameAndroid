package com.yunke.xiaovo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.bean.Porker;
import com.yunke.xiaovo.utils.BitmapUtil;

/**
 *
 */
public class PorkerView extends View {
    private Bitmap mPorkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.porker_bg);
    private Bitmap mPorkerText; // 扑克文字
    private Bitmap mPorkerType; //扑克类型
    private Bitmap mPorkerTextRB; // 扑克文字右下角
    private Bitmap mPorkerTypeRB; //扑克类型右下角
    private float mWidth;
    private float mHeight;
    private Paint mPaint;
    private boolean isSelected;
    private RectF mSelectedRect = new RectF();

    private static final int[] PORKER_RED_TEXT_ID = {R.drawable.porker_red_1, R.drawable.porker_red_2,
            R.drawable.porker_red_3, R.drawable.porker_red_4, R.drawable.porker_red_5, R.drawable.porker_red_6,
            R.drawable.porker_red_7, R.drawable.porker_red_8, R.drawable.porker_red_9, R.drawable.porker_red_10, R.drawable.porker_red_11,
            R.drawable.porker_red_12, R.drawable.porker_red_13};
    private static final int[] PORKER_BLACK_TEXT_ID = {R.drawable.porker_black_1, R.drawable.porker_black_2,
            R.drawable.porker_black_3, R.drawable.porker_black_4, R.drawable.porker_black_5, R.drawable.porker_black_6,
            R.drawable.porker_black_7, R.drawable.porker_black_8, R.drawable.porker_black_9, R.drawable.porker_black_10, R.drawable.porker_black_11,
            R.drawable.porker_black_12, R.drawable.porker_black_13};
    private static final int[] PORKER_TYPE_ID = {0, R.drawable.porker_red_heart, R.drawable.porker_plum_blossom,
            R.drawable.porker_black_heart, R.drawable.porker_block,
            R.drawable.porker_small_king, R.drawable.porker_big_king};


    public PorkerView(Context context) {
        this(context, null);
    }

    public PorkerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorkerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mSelectedRect.right = mWidth = mPorkerBitmap.getWidth();
        mSelectedRect.bottom = mHeight = mPorkerBitmap.getHeight();
    }


    public void setPorker(int porkerId, int porkerType) {
        porkerId = porkerId % 13;
        mPorkerText = null;

        if (porkerType == Porker.RED_HEART || porkerType == Porker.BLOCK) {
            mPorkerText = BitmapFactory.decodeResource(getResources(), PORKER_RED_TEXT_ID[porkerId]);
        } else if (porkerType == Porker.BLACK_HEART || porkerType == Porker.PLUM_BLOSSOM) {
            mPorkerText = BitmapFactory.decodeResource(getResources(), PORKER_BLACK_TEXT_ID[porkerId]);
        }
        mPorkerType = BitmapFactory.decodeResource(getResources(), PORKER_TYPE_ID[porkerType]);
        if (mPorkerText != null) {
            mPorkerTextRB = convert(mPorkerText);
            mPorkerTypeRB = convert(mPorkerType);
        }
    }


    public void setWidth(int width) {
        if (mWidth == width) {
            return;
        }
        this.mWidth = width;
        setMeasuredDimension((int) mWidth, (int) mHeight);
        scaleAllBitmap();
        invalidate();
    }

    public void setHeight(int height) {
        if (mHeight == height) {
            return;
        }
        this.mHeight = height;
        setMeasuredDimension((int) mWidth, (int) mHeight);
        scaleAllBitmap();
        invalidate();
    }


    public void setWidthAndHeight(int width, int height) {
        if (mWidth == width && mHeight == height) {
            return;
        }
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        setMeasuredDimension((int) mWidth, (int) mHeight);
        scaleAllBitmap();
        invalidate();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        invalidate();
    }

    public void setSelected() {
        this.isSelected = !isSelected;
        invalidate();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(0xff000000);
        canvas.drawBitmap(mPorkerBitmap, 0, 0, mPaint);
        if (mPorkerText != null) {
            canvas.drawBitmap(mPorkerText, 0, 0, mPaint);
            canvas.drawBitmap(mPorkerType, mPorkerText.getWidth() / 2 - mPorkerType.getWidth() / 2, mPorkerText.getHeight(), mPaint);
            canvas.drawBitmap(mPorkerTextRB, mPorkerBitmap.getWidth() - mPorkerTextRB.getWidth(), mPorkerBitmap.getHeight() - mPorkerTextRB.getHeight(), mPaint);
            canvas.drawBitmap(mPorkerTypeRB, mPorkerBitmap.getWidth() - mPorkerTypeRB.getWidth() - (mPorkerTextRB.getWidth()
                    / 2 - mPorkerTypeRB.getWidth() / 2), mPorkerBitmap.getHeight() - mPorkerTextRB.getHeight() - mPorkerTypeRB.getHeight(), mPaint);
        } else {
            canvas.drawBitmap(mPorkerType, 0, 0, mPaint);
        }

        if (isSelected) {
            mPaint.setColor(0x33000000);
            canvas.drawRoundRect(mSelectedRect, 15, 15, mPaint);
        }


    }


    private Bitmap convert(Bitmap a) {
        int w = a.getWidth();
        int h = a.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        Matrix m = new Matrix();
        m.postScale(1, -1);   //镜像垂直翻转
        m.postScale(-1, 1);   //镜像水平翻转
        Bitmap new2 = Bitmap.createBitmap(a, 0, 0, w, h, m, true);
        cv.drawBitmap(new2, new Rect(0, 0, new2.getWidth(), new2.getHeight()), new Rect(0, 0, w, h), null);
        return newb;

    }




    private void scaleAllBitmap() {
        float scaleWidth = mWidth / mPorkerBitmap.getWidth();
        float scaleHeight = mHeight / mPorkerBitmap.getHeight();

        mPorkerBitmap = BitmapUtil.scaleBitmap(mPorkerBitmap, scaleWidth, scaleHeight);
        mPorkerType = BitmapUtil.scaleBitmap(mPorkerType, scaleWidth, scaleHeight);
        if (mPorkerText != null) {
            mPorkerText = BitmapUtil.scaleBitmap(mPorkerText, scaleWidth, scaleHeight);
            mPorkerTextRB = BitmapUtil.scaleBitmap(mPorkerTextRB, scaleWidth, scaleHeight);
            mPorkerTypeRB = BitmapUtil.scaleBitmap(mPorkerTypeRB, scaleWidth, scaleHeight);
        }
    }

}
