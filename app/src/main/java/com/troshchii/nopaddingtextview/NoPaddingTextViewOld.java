package com.troshchii.nopaddingtextview;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;


/**
 This class was copied from the https://github.com/SenhLinsh/NoPaddingTextView Library
 Because we can't remove completely padding in the {@link TextView}.
 android:includeFontPadding="false" also does not remove completely padding.
 */
public class NoPaddingTextViewOld extends android.support.v7.widget.AppCompatTextView {

    private int mAdditionalPadding;

    public NoPaddingTextViewOld(Context context) {
        super(context);
        init();
    }

    public NoPaddingTextViewOld(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setIncludeFontPadding(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int yOff = -mAdditionalPadding / 6;
        canvas.translate(0, yOff);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getAdditionalPadding();

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            int measureHeight = measureHeight(getText().toString(), widthMeasureSpec);

            int height = measureHeight - mAdditionalPadding;
            height += getPaddingTop() + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight(String text, int widthMeasureSpec) {
        float textSize = getTextSize();

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setText(text);
        textView.measure(widthMeasureSpec, 0);
        return textView.getMeasuredHeight();
    }

    private int getAdditionalPadding() {
        float textSize = getTextSize();

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setLines(1);
        textView.measure(0, 0);
        int measuredHeight = textView.getMeasuredHeight();
        if (measuredHeight - textSize > 0) {
            mAdditionalPadding = (int) (measuredHeight - textSize);
//            Timber.d("NoPaddingTextView. onMeasure: height=" + measuredHeight + " textSize=" + textSize + " mAdditionalPadding=" + mAdditionalPadding);
        }
        return mAdditionalPadding;
    }
}