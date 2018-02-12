package com.yunke.xiaovo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.bean.DDZPorker;
import com.yunke.xiaovo.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 斗地主扑克View
 */
public class PorkerListView extends ViewGroup {


    public static final int CENTER = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private float mWidth; // View宽度
    private float marginX; // 扑克牌之间的间距
    private float startX; // 扑克牌开始位置
    private float porkerMarginTop; //扑克牌距上位置

    private float porkerWidth;
    private float porkerHeight;
    private boolean isClick;
    private float downX;
    private List<Integer> clickIndex = new ArrayList<>();
    private int gravity = CENTER;
    private int viewCount;
    private int lastSelectedIndex = -1;

    public PorkerListView(Context context) {
        this(context, null);
    }

    public PorkerListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorkerListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.porker_bg);
        porkerWidth = bitmap.getWidth();
        porkerHeight = bitmap.getHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        layoutChildView();
    }

    private void layoutChildView() {
        int left = (int) startX;
        int startY;
        for (int i = 0; i < viewCount; i++) {
            PorkerView porkerView = (PorkerView) getChildAt(i);
            startY = (int) porkerMarginTop;
            for (int j = 0; j < clickIndex.size(); j++) {
                if (i == clickIndex.get(j)) {
                    startY = 0;
                }
            }
            porkerView.layout(left, startY, (int) (left + porkerWidth), (int) (startY + porkerHeight));
            left += marginX;
        }
    }


    public void setGravity(int gravity) {
        this.gravity = gravity;

    }


    public void setPorkerWidthAndHeight(float porkerWidth, float porkerHeight) {
        if (this.porkerWidth == porkerWidth && this.porkerHeight == porkerHeight) {
            return;
        }
        this.porkerWidth = porkerWidth;
        this.porkerHeight = porkerHeight;
        for (int i = 0; i < viewCount; i++) {
            PorkerView porkerView = (PorkerView) getChildAt(i);
            porkerView.setWidthAndHeight(porkerWidth, porkerHeight);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClick || viewCount == 0) return super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.downX = event.getX();
                if (isInRange(downX)) {
                    int index = getCurrentIndex(downX);
                    getPorkerView(index).setSelected();
                    lastSelectedIndex = index;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                if (isInRange(moveX)) {
                    int index = getCurrentIndex(moveX);
                    if (lastSelectedIndex != index) {
                        if (getPorkerView(index).isSelected() && lastSelectedIndex != -1) {
                            completionIndex(lastSelectedIndex, index);
                            getPorkerView(lastSelectedIndex).setSelected();
                        } else {
                            completionIndex(index, lastSelectedIndex);
                            getPorkerView(index).setSelected();
                        }
                    }
                    lastSelectedIndex = index;
                }
                break;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < viewCount; i++) {
                    PorkerView porkerView = getPorkerView(i);
                    if (porkerView.isSelected()) {
                        boolean isRemove = false;
                        for (int j = 0; j < clickIndex.size(); j++) {
                            if (i == clickIndex.get(j)) {
                                clickIndex.remove(j);
                                isRemove = true;
                                break;
                            }
                        }
                        if (!isRemove) {
                            clickIndex.add(i);
                        }
                    }
                    porkerView.setSelected(false);
                }
                layoutChildView();
                break;
        }

        return true;
    }

    public void clear() {
        removeAllViews();
        viewCount = 0;
    }

    public void clearIndex() {
        clickIndex.clear();
        layoutChildView();
    }

    public List<Integer> getClickIndex() {
        Collections.sort(clickIndex);
        return clickIndex;
    }

    private void completionIndex(int startIndex, int endIndex) {
        int completionIndex = startIndex - endIndex;
        if (completionIndex > 1) {
            for (int i = endIndex + 1; i < startIndex; i++) {
                getPorkerView(i).setSelected();
            }
            return;
        }
        completionIndex = endIndex - startIndex;
        if (completionIndex > 1) {
            for (int i = startIndex + 1; i < endIndex; i++) {
                getPorkerView(i).setSelected();
            }
        }


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

    private boolean isInRange(float x) {
        return x >= startX && x <= startX + (marginX * (viewCount - 1)) + porkerWidth;
    }


    private int getCurrentIndex(float x) {
        if (isInRange(x)) {
            int index = (int) ((x - startX) / marginX);
            if (index > viewCount - 1) {
                index = viewCount - 1;
            }
            return index;
        }
        return -1;

    }

    private PorkerView getPorkerView(int index) {
        return (PorkerView) getChildAt(index);
    }


    public void isClick(boolean isClick) {
        this.isClick = isClick;
//        setMeasuredDimension((int) mWidth, (int) (porkerHeight + porkerHeight / 2));
    }

    public void upDatePorker(List<DDZPorker> porkers) {
        if (porkers == null || porkers.size() == 0) {
            return;
        }
        updateView(porkers);
        if (mWidth > 0) {
            initLocation();
            layoutChildView();
        }
    }

    private void initLocation() {
        if (viewCount > 0) {
            marginX = (mWidth - porkerWidth) / viewCount;
        }

        if (marginX > porkerWidth / 2) {
            marginX = porkerWidth / 2;
        }
        if (gravity == LEFT) {
            startX = 0;
        } else if (gravity == CENTER) {
            startX = (mWidth / 2) - ((marginX * viewCount + (porkerWidth - marginX)) / 2);
        } else if (gravity == RIGHT) {
            startX = mWidth - (marginX * viewCount + (porkerWidth - marginX));
        }
    }

    private void updateView(List<DDZPorker> porkers) {
        int count = getChildCount() - porkers.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                removeViewAt(0);
            }
        }
        if (count < 0) {
            for (int i = 0; i < Math.abs(count); i++) {
                PorkerView porkerView = new PorkerView(getContext());
                porkerView.setWidthAndHeight((int) porkerWidth, (int) porkerHeight);
                addView(porkerView);
            }
        }

        for (int i = 0; i < porkers.size(); i++) {
            PorkerView porkerView = (PorkerView) getChildAt(i);
            porkerView.setPorker(porkers.get(i).porkerId, porkers.get(i).porkerType);
            porkerView.invalidate();
        }
        viewCount = getChildCount();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        porkerMarginTop = (mHeight - porkerHeight) / 2;

        if (viewCount > 0) {
            initLocation();
            layoutChildView();
        }
        LogUtil.i("onSizeChanged width = " + mWidth);
        LogUtil.i("onSizeChanged mHeight = " + mHeight);
        LogUtil.i("onSizeChanged porkerMarginTop = " + porkerMarginTop);
        LogUtil.i("onSizeChanged porkerHeight = " + porkerHeight);
    }

}
