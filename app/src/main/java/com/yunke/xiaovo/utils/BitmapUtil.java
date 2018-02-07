package com.yunke.xiaovo.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 *
 */
public class BitmapUtil {




    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                true);
    }


}
