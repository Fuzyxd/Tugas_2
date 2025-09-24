package com.example.tugas_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BubbleSpan extends ReplacementSpan {

    private final Paint mBackgroundPaint;
    private final Paint mTextPaint;
    private final int mTextColor;
    private final float mCornerRadius;
    private final float mPaddingHorizontal;
    private final float mPaddingVertical;

    public BubbleSpan(Context context, int backgroundColor, int textColor, boolean isSentMessage) {
        // isSentMessage can be used later for different bubble shapes (e.g., tail)
        // For now, it mainly helps select colors.

        mTextColor = textColor;

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);

        mCornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());
        mPaddingHorizontal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        mPaddingVertical = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        mTextPaint.setTextSize(paint.getTextSize());
        mTextPaint.setTypeface(paint.getTypeface());

        if (fm != null) {
            Paint.FontMetrics paintFm = mTextPaint.getFontMetrics();
            fm.ascent = (int) paintFm.ascent;
            fm.descent = (int) paintFm.descent;
            fm.top = (int) paintFm.top;
            fm.bottom = (int) paintFm.bottom;
        }
        return (int) (mPaddingHorizontal + mTextPaint.measureText(text, start, end) + mPaddingHorizontal);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, @NonNull Paint paint) {

        mTextPaint.setTextSize(paint.getTextSize());
        mTextPaint.setTypeface(paint.getTypeface());
        mTextPaint.setColor(this.mTextColor);

        float textWidth = mTextPaint.measureText(text, start, end);
        Paint.FontMetrics textMetrics = mTextPaint.getFontMetrics();
        float bubbleTop = y + textMetrics.ascent - mPaddingVertical;
        float bubbleBottom = y + textMetrics.descent + mPaddingVertical;

        RectF rect = new RectF(x, bubbleTop, x + textWidth + (2 * mPaddingHorizontal), bubbleBottom);
        canvas.drawRoundRect(rect, mCornerRadius, mCornerRadius, mBackgroundPaint);

        canvas.drawText(text, start, end, x + mPaddingHorizontal, y, mTextPaint);
    }
}
