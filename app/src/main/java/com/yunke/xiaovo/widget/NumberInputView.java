package com.yunke.xiaovo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.utils.LogUtil;

/**
 *
 */
public class NumberInputView extends ViewGroup {
    private static final int[] BACKGROUND_ID = {R.drawable.number_1,R.drawable.number_2,
            R.drawable.number_3,R.drawable.number_4,R.drawable.number_5,R.drawable.number_6,
            R.drawable.number_7,R.drawable.number_8,R.drawable.number_9,R.drawable.number_clear,R.drawable.number_0, R.drawable.number_delete,};
    private float mChildWidth;
    private float mChildHeight;
    private float rowHeight; //每个行的高度
    private float columnWidth; //每个列的宽度
    private static final int COLUMN_COUNT = 3;//列数
    private static final int ROW_COUNT = 4;//行数
    private OnNumberClickListener mOnNumberClickListener;

    public NumberInputView(Context context) {
        this(context, null);
    }

    public NumberInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int position = 0;
        for (int resource : BACKGROUND_ID) {
            LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.view_number_button, null);
            final CommonButton commonButton = (CommonButton) linearLayout.getChildAt(0);
            final int finalPosition = position;
            commonButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnNumberClickListener != null) {
                        mOnNumberClickListener.onNumberClick(commonButton, finalPosition);
                    }
                }
            });
            commonButton.setBackgroundResource(resource);
            addView(linearLayout);
            position++;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtil.i("onSizeChanged paddingLeft = " + getPaddingLeft());
        float mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        float mHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mChildWidth = getChildAt(0).getMeasuredWidth();
        mChildHeight = getChildAt(0).getMeasuredHeight();
        rowHeight = mHeight / ROW_COUNT;
        columnWidth = mWidth / COLUMN_COUNT;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.i("paddingLeft = " + getPaddingLeft());
        LogUtil.i("left = " + (columnWidth / 2 - mChildWidth / 2));
        float left = columnWidth / 2 - mChildWidth / 2 + getPaddingLeft();
        LogUtil.i("left = " + left);

        float top = rowHeight / 2 - mChildHeight / 2 - rowHeight + getPaddingTop();
        float right;
        float bottom;
        for (int i = 0; i < BACKGROUND_ID.length; i++) {
            if (i % COLUMN_COUNT == 0) {
                left = columnWidth / 2 - mChildWidth / 2 + getPaddingLeft();
                top += rowHeight;
            }
            right = left + mChildWidth;
            bottom = top + mChildHeight;

            getChildAt(i).layout((int) left, (int) top, (int) right, (int) bottom);
            left += columnWidth;
        }
    }


    public void setOnNumberClickListener(OnNumberClickListener onNumberClickListener) {
        this.mOnNumberClickListener = onNumberClickListener;

    }

    public interface OnNumberClickListener {
        void onNumberClick(CommonButton button, int position);
    }

}
