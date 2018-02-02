package com.yunke.xiaovo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 公共的TextView
 */
public class CommonTextView extends android.support.v7.widget.AppCompatTextView {
    public CommonTextView(Context context) {
        this(context, null);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
