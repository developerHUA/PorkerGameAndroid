package com.yunke.xiaovo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.bean.DDZPorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * 斗地主扑克View
 */
public class DDZPorkerView extends View {


    public static final int CENTER = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private float mWidth; // View宽度
    private float marginX; // 扑克牌之间的间距
    private float startX; // 扑克牌开始位置
    private float porkerWidth;
    private float porkerHeight;
    private float porkerMarginTop; //扑克牌距上位置
    private static final float PNG_WIDTH = 10; // 图片透明区域大小
    private boolean isClick;
    private float downX;
    private RectF grayRectF = new RectF();
    private List<DDZPorker> mPorkers = new ArrayList<>();
    private Paint mPorkerPaint = new Paint();
    private Paint mGrayPaint = new Paint();
    private List<Integer> clickIndex = new ArrayList<>();
    private Path mPath = new Path();
    private int gravity = CENTER;

    public DDZPorkerView(Context context) {
        this(context, null);
    }

    public DDZPorkerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DDZPorkerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPorkerPaint.setAntiAlias(true);
        mGrayPaint.setAntiAlias(true);
        mGrayPaint.setColor(0x44000000);
        Bitmap bitmap = decodeResource(getResources(), R.drawable.fang1);
        this.porkerWidth = bitmap.getWidth();
        this.porkerHeight = bitmap.getHeight();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int index = 0;
        mPath.reset();

        for (DDZPorker porker : mPorkers) {
            float startY = porkerMarginTop;
            if (porker.isClick) {
                startY = 0;
            }

            canvas.drawBitmap(decodeResource(getResources(), porker.getResourceId()), startX + (marginX * index), startY, mPorkerPaint);
            index++;
        }

        if (clickIndex.size() <= 0 || grayRectF.left == 0) {

            mPath.addRect(grayRectF, Path.Direction.CW);
            canvas.drawPath(mPath, mGrayPaint);
            return;
        }


        int startIndex = (int) ((grayRectF.left - startX) / marginX);
        int endIndex = (int) ((grayRectF.right - startX) / marginX);
        if (endIndex >= mPorkers.size()) {
            endIndex = mPorkers.size();
        }

        if (startIndex <= -1) {
            startIndex = 0;
        }

        for (int i = startIndex; i < endIndex; i++) {
            if (mPorkers.get(i).isClick && i < mPorkers.size() - 1 && !mPorkers.get(i + 1).isClick) {
                setPorkerMargin(startX + i * marginX, 0);
                setPorkerLeftUp(startX + i * marginX);
            } else if (i >= mPorkers.size() - 1 && mPorkers.get(i).isClick) {
                setPorkerGray(startX + i * marginX, 0);
            } else if (mPorkers.get(i).isClick) {
                setPorkerMargin(startX + i * marginX, 0);
            } else if (i < mPorkers.size() - 1 && mPorkers.get(i + 1).isClick) {
                setPorkerMargin(startX + i * marginX, porkerMarginTop);
                setPorkerRightDown(startX + i * marginX);
            } else if (i < mPorkers.size() - 1 && !mPorkers.get(i + 1).isClick) {
                setPorkerMargin(startX + i * marginX, porkerMarginTop);
            } else if (i >= mPorkers.size() - 1) {
                setPorkerGray(startX + (mPorkers.size() - 1) * marginX, porkerMarginTop);

            }


        }


        canvas.drawPath(mPath, mGrayPaint);
//        canvas.drawRoundRect(grayRectF, 5, 5, mGrayPaint);
        mPath.close();

    }


    private void setPorkerMargin(float left, float top) {
        mPath.moveTo(left, top);
        mPath.lineTo(left + marginX + PNG_WIDTH, top);
        mPath.lineTo(left + marginX + PNG_WIDTH, top + porkerHeight);
        mPath.lineTo(left, top + porkerHeight);
    }

    private void setPorkerRightDown(float left) {
        mPath.moveTo(left + marginX + PNG_WIDTH, porkerHeight);
        mPath.lineTo(left + porkerWidth, porkerHeight);
        mPath.lineTo(left + porkerWidth, porkerMarginTop + porkerHeight);
        mPath.lineTo(left + marginX + PNG_WIDTH, porkerMarginTop + porkerHeight);
        mPath.lineTo(left + marginX + PNG_WIDTH, porkerHeight);
    }

    private void setPorkerLeftUp(float left) {
        mPath.moveTo(left + marginX, 0);
        mPath.lineTo(left + porkerWidth, 0);
        mPath.lineTo(left + porkerWidth, porkerMarginTop);
        mPath.lineTo(left + marginX, porkerMarginTop);
    }


    private void setPorkerGray(float left, float top) {
        mPath.moveTo(left, top);
        mPath.lineTo(left + porkerWidth, top);
        mPath.lineTo(left + porkerWidth, top + porkerHeight);
        mPath.lineTo(left, top + porkerHeight);
    }


    public void setGravity(int gravity) {
        this.gravity = gravity;

    }

    boolean isFlag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClick) return super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.downX = event.getX();
                isFlag = false;
                if (isInRange(downX)) {

                    setRectF(downX);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                if (isInRange(moveX)) {
                    if (!isFlag) {
                        downX = moveX;
                        setRectF(downX);
                    }

                    isFlag = true;
                    int index = (int) ((moveX - startX) / marginX);

                    if (moveX - downX > 10) {
                        if (index == mPorkers.size() - 1) {

                            grayRectF.right = (index) * marginX + porkerWidth + startX;
                        } else if (index < mPorkers.size()) {
                            grayRectF.right = (index + 1) * marginX + startX + 10;

                        }
                    } else if (moveX - downX < -10) {
                        if (index >= mPorkers.size()) {
                            grayRectF.left = (index - 1) * marginX + startX;
                        } else {
                            grayRectF.left = index * marginX + startX;
                        }
                    }


                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                clickIndex.clear();
                int startIndex = (int) ((grayRectF.left - startX) / marginX);
                int endIndex = (int) ((grayRectF.right - startX) / marginX);
                if (endIndex >= mPorkers.size()) {
                    endIndex = mPorkers.size();
                }

                if (startIndex <= -1) {
                    startIndex = 0;
                }

                for (int i = startIndex; i < endIndex; i++) {
                    mPorkers.get(i).isClick = !mPorkers.get(i).isClick;
                }


                for (int i = 0; i < mPorkers.size(); i++) {
                    if (mPorkers.get(i).isClick) {
                        clickIndex.add(i);
                    }
                }

                grayRectF.left = 0;
                grayRectF.right = 0;

                invalidate();
                break;


        }

        return true;
    }

    public void clearIndex() {
        clickIndex.clear();
        invalidate();
    }

    public List<Integer> getClickIndex() {
        Collections.sort(clickIndex);
        return clickIndex;
    }


    private void setRectF(float x) {
        int index = (int) ((x - startX) / marginX);

        if (index >= mPorkers.size() - 1) {
            grayRectF.left = (mPorkers.size() - 1) * marginX + startX;
            grayRectF.right = grayRectF.left + porkerWidth;
        } else {
            grayRectF.right = (index + 1) * marginX + startX + 10;
            grayRectF.left = index * marginX + startX;
        }

    }

    private boolean isInRange(float x) {
        return x >= startX && x <= startX + (marginX * (mPorkers.size() - 1)) + porkerWidth;
    }


    public void isClick(boolean isClick) {
        this.isClick = isClick;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (isClick) {
            setMeasuredDimension(width, (int) (porkerHeight + porkerHeight / 2));

        } else {
            setMeasuredDimension(width, (int) (porkerHeight));

        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        grayRectF.top = (mHeight - porkerHeight) / 2;

        grayRectF.bottom = grayRectF.top + porkerHeight;
        porkerMarginTop = (mHeight - porkerHeight) / 2;
    }


    public void upDatePorker(List<DDZPorker> porkers) {
        this.mPorkers = porkers;
        if (porkers == null || porkers.size() == 0) {
            return;
        }
        if (this.mPorkers.size() > 0)
            marginX = (mWidth - porkerWidth) / this.mPorkers.size();
        if (marginX > porkerWidth / 2) {
            marginX = porkerWidth / 2;
        }
        if (gravity == LEFT) {
            startX = 0;
        } else if (gravity == CENTER) {
            startX = (mWidth / 2) - ((marginX * porkers.size() + (porkerWidth - marginX)) / 2);
        } else if (gravity == RIGHT) {
            startX = mWidth - (marginX * porkers.size() + (porkerWidth - marginX));
        }
        invalidate();
    }

    public void clear() {
        mPorkers = new ArrayList<>();
        invalidate();
    }


    public List<DDZPorker> getPorkers() {
        return mPorkers;
    }
}
