package com.yunke.xiaovo.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;
import com.yunke.xiaovo.R;
import com.yunke.xiaovo.manage.AppManager;

/**
 * picasso 圆角
 */
public class CropSquareTransformation implements Transformation {
    private RectF mRectF = new RectF();
    private float roundSize = AppManager.getInstance().getResources().getDimension(R.dimen.x20);

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        mRectF.left = 0;
        mRectF.right = bitmap.getWidth();
        mRectF.top = 0;
        mRectF.bottom = bitmap.getHeight();
        canvas.drawRoundRect(mRectF, roundSize, roundSize, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
