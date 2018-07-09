package ru.timrlm.testproject.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

public class BitmapUtil {

    public static Bitmap mirrorBitmap(Bitmap bmp){
        Matrix matrix = new Matrix();
        matrix.setScale( -1 , 1 );
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
    }

    public static Bitmap monochrome(Bitmap bmp){
        Bitmap bmpMonochrome = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bmpMonochrome;
    }

    public static Bitmap rotate(Bitmap bmp, float degrees) {
        if (degrees % 360 != 0) {
            Matrix rotMat = new Matrix();
            rotMat.postRotate(degrees);
            if (bmp != null) {
                Bitmap dst = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), rotMat, false);
                return dst;
            }
        } else { return bmp; }
        return null;
    }
}
