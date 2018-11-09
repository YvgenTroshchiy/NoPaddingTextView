package com.troshchii.nopaddingtextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView


/**
 * Because Attribute android:includeFontPadding="false" and android:background="@null"
 * do not remove completely padding.
 */
class NoPaddingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private val tag = NoPaddingTextView::class.java.simpleName

    private val paint = Paint()
    private val bounds = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateTextParams()
        setMeasuredDimension(bounds.width() + 1, -bounds.top + 1)
    }

    private fun calculateTextParams(): String {
        val text = text.toString()
        val textLength = text.length

        paint.textSize = textSize
        paint.getTextBounds(text, 0, textLength, bounds)

        return text
    }

    override fun onDraw(canvas: Canvas) {
        // If we call this method only in the [onMeasure] we will not see "text" in the Android preview
        val text = calculateTextParams()

        val left = bounds.left
        val bottom = bounds.bottom

        Log.d(tag, "onDraw: x: $text, y: $bottom")

        bounds.offset(-bounds.left, -bounds.top)

        paint.isAntiAlias = true
        paint.color = currentTextColor

        canvas.drawText(
            text,
            /* x= */(-left).toFloat(),
            /* y= */ (bounds.bottom - bottom).toFloat(),
            paint
        )
    }
}