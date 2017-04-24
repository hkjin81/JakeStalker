package kr.hkjin.jakestalker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

/**
 * Created by hkjin81 on 2017. 4. 24..
 */

public class RoundedRectTransform implements Transformation {
    private float rx = 10;
    private float ry = 10;
    private static float dpToPx(float dp)
    {
        return (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public RoundedRectTransform() {}

    public RoundedRectTransform(float rxDp, float ryDp) {
        this.rx = dpToPx(rxDp);
        this.ry = dpToPx(ryDp);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int w = source.getWidth();
        int h = source.getHeight();

        Bitmap squaredBitmap = Bitmap.createBitmap(source, 0, 0, w, h);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        if (rx * 2f > w) {
            rx = w / 2f;
        }
        if (ry * 2f > h) {
            ry = h / 2f;
        }
        canvas.drawRoundRect(new RectF(0f, 0f, (float)w, (float)h), rx, ry, paint);
        squaredBitmap.recycle();

        Paint linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(dpToPx(1));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setAntiAlias(true);
        canvas.drawRoundRect(new RectF(1f, 1f, (float)w - 1, (float)h - 1), rx, ry, linePaint);

        return bitmap;
    }



    @Override
    public String key() {
        return String.format("roundedRect %f %f", rx, ry);
    }
}
