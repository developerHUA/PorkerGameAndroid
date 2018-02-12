package com.yunke.xiaovo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 *  不按背景大小的包裹内容View
 */
public class WrapContentView extends ViewGroup {

    private int widthMeasureSpec;
    private int heightMeasureSpec;

    public WrapContentView(Context context) {
        this(context, null);
    }

    public WrapContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void reSize() {
        View view = getChildAt(0);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(),
                height + getPaddingTop() + getPaddingBottom());

        childLayout();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        View view = getChildAt(0);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(),
                height + getPaddingTop() + getPaddingBottom());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        childLayout();
    }

    private void childLayout() {
        View view = getChildAt(0);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        view.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + width, getPaddingTop() + height);
    }


}
