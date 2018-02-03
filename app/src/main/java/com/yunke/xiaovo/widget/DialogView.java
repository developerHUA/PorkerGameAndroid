package com.yunke.xiaovo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yunke.xiaovo.R;

/**
 *
 */
public class DialogView extends ViewGroup {

    private int widthMeasureSpec;
    private int heightMeasureSpec;

    public DialogView(Context context) {
        this(context, null);
    }

    public DialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.drawable.dialog_bg);
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
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();

        measureChildren(widthMeasureSpec, heightMeasureSpec);
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
