// File: com/example/tugas_2/BubbleSpan.java
package com.example.tugas_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

public class BubbleSpan extends ReplacementSpan {
    private final int backgroundColor;
    private final int textColor;
    private final float cornerRadius;
    private final float paddingHorizontal;
    private final float paddingTop;
    private final float paddingBottom;
    private final boolean isSent;

    public BubbleSpan(Context context, int backgroundColor, int textColor, boolean isSent) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.isSent = isSent; // This can be used for more advanced bubble shapes later

        this.cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());
        this.paddingHorizontal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        this.paddingTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics());
        this.paddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        // Measure text and add padding for width calculation
        return (int) (paddingHorizontal + paint.measureText(text, start, end) + paddingHorizontal);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        // Calculate the actual width of the text to be drawn
        float actualTextWidth = paint.measureText(text, start, end);

        // Calculate bubble top and bottom based on text metrics and padding
        // y is the baseline of the text. fm.ascent is negative.
        float bubbleTop = y + paint.ascent() - paddingTop;
        float bubbleBottom = y + paint.descent() + paddingBottom;

        // Define the rectangle for the bubble background
        // x is the starting x-coordinate for drawing the text.
        RectF rect = new RectF(x, bubbleTop, x + actualTextWidth + (2 * paddingHorizontal), bubbleBottom);

        // Draw the background bubble
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        // Draw the text on top of the bubble, offset by horizontal padding
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x + paddingHorizontal, y, paint);
    }
}
