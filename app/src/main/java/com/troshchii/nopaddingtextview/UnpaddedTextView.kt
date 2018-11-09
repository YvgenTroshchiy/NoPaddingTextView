package com.troshchii.nopaddingtextview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View


fun Resources.spToPx(sp: Int): Int = (sp * displayMetrics.scaledDensity).toInt()

/**
 * Because Attribute android:includeFontPadding="false" and android:background="@null"
 * do not remove completely padding.
 * We don't need here [TextView] because we draw it on the canvas.
 */
class UnpaddedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val tag = UnpaddedTextView::class.java.simpleName

    private val DEFAULT_TEXT_SIZE = resources.spToPx(16).toFloat()
    private val DEFAULT_TEXT_COLOR = ContextCompat.getColor(context, android.R.color.black)

    private var text = ""
    private var textSize = DEFAULT_TEXT_SIZE
    private var textColor = DEFAULT_TEXT_COLOR

    private val paint = Paint()
    private val bounds = Rect()

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.UnpaddedTextView)
        try {
            text = attributes.getString(R.styleable.UnpaddedTextView_text) ?: ""
            textSize = attributes.getDimension(R.styleable.UnpaddedTextView_textSize, DEFAULT_TEXT_SIZE)
            textColor = attributes.getColor(R.styleable.UnpaddedTextView_textColor, DEFAULT_TEXT_COLOR)
        } finally {
            attributes.recycle()
        }

        Log.i(tag, "text: $text, textSize: $textSize, textColor: $textColor")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateTextParams()
        setMeasuredDimension(bounds.width() + 1, -bounds.top + 1)
    }

    private fun calculateTextParams(): String {
        val textLength = text.length

        paint.textSize = textSize
        paint.getTextBounds(text, 0, textLength, bounds)

        return text
    }

    override fun onDraw(canvas: Canvas?) {
        // If we call this method only in the [onMeasure] we will not see "text" in the Android preview
        val text = calculateTextParams()

        val left = bounds.left
        val bottom = bounds.bottom

        Log.i(tag, "onDraw: x: $text, y: $bottom")

        bounds.offset(-bounds.left, -bounds.top)

        paint.isAntiAlias = true
        paint.color = textColor

        canvas?.drawColor(ContextCompat.getColor(context, R.color.colorAccent))
        canvas?.drawText(
            text,
            /* x= */(-left).toFloat(),
            /* y= */ (bounds.bottom - bottom).toFloat(),
            paint
        )
    }
}