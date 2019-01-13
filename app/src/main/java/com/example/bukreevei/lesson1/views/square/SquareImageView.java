package com.example.bukreevei.lesson1.views.square;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class SquareImageView extends AppCompatImageView {

    private static final float DEFAULT_BOARDER_STROKE = 5;

    private int borderColor;
    private int borderWidth;

    private Bitmap bitmap;

    private boolean removeBorder;

    public SquareImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.removeBorder = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int desiredHeight = 100;
        final int desiredWidth = 100;

        final int measuredHeight = getSize(desiredHeight, heightMeasureSpec);
        final int measuredWidth = getSize(desiredWidth, widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int getSize(final int desiredSize, final int measureSpec) {
        final int mode = MeasureSpec.getMode(measureSpec);
        final int parrentSize = MeasureSpec.getSize(measureSpec);

        switch (mode) {

            case MeasureSpec.EXACTLY:
                return parrentSize;

            case MeasureSpec.AT_MOST:
                if (desiredSize < parrentSize) {
                    return desiredSize;
                } else {
                    return parrentSize;
                }
            case MeasureSpec.UNSPECIFIED:
            default:
                return desiredSize;
        }

    }

    @Override
    protected void onDraw(final Canvas canvas) {

        final Bitmap imageBitmap = getBitmapResource();
        Bitmap temporaryBitmap;
        if (imageBitmap != null) {
            temporaryBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);
        } else {
            return;
        }

        final int w = getWidth();

        final Bitmap roundBitmap = getRoundedCroppedBitmap(cropBitmap(temporaryBitmap), w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    private Bitmap getBitmapResource() {
        if (bitmap == null) {
            final Drawable drawable = getDrawable();
            if (drawable == null) {
                return null;
            }

            if (getWidth() == 0 || getHeight() == 0) {
                return null;
            }

            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            return bitmap;
        }
    }

    private Bitmap cropBitmap(final Bitmap sourceBmp) {
        Bitmap outputBmp;

        if (sourceBmp.getWidth() > sourceBmp.getHeight()) {
            outputBmp = Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.getHeight(), sourceBmp.getHeight());
        } else if (sourceBmp.getWidth() < sourceBmp.getHeight()) {
            outputBmp = Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.getWidth(), sourceBmp.getWidth());
        } else {
            outputBmp = sourceBmp;
        }

        return outputBmp;
    }

    private Bitmap getRoundedCroppedBitmap(final Bitmap bitmap, final int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            finalBitmap = bitmap;
        }

        final Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(new RectF(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight()), 15, 15, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        if (!removeBorder) {
            canvas.drawRoundRect(new RectF(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight()), 15, 15, getBorderPaint());
        }



        return output;
    }

    private Paint getBorderPaint() {
        final Paint borderPaint = new Paint();
        if (borderColor != 0) {
            borderPaint.setColor(borderColor);
        } else {
            borderPaint.setColor(Color.WHITE);
        }
        if (borderWidth != 0) {
            borderPaint.setStrokeWidth(borderWidth);
        } else {
            borderPaint.setStrokeWidth(DEFAULT_BOARDER_STROKE);
        }

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);

        return borderPaint;
    }

    public void setBitmap(Bitmap bmp) {
        this.bitmap = bmp;
    }

    public void setBorderColor(int colorResource) {
        this.borderColor = colorResource;
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
    }

    public void removeBorder(boolean removeBorder) {
        this.removeBorder = removeBorder;
    }

}
