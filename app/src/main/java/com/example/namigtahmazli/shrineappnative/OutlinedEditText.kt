package com.example.namigtahmazli.shrineappnative

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import kotlin.math.sqrt

class OutlinedEditText : LinearLayout, View.OnTouchListener, View.OnFocusChangeListener {
    private val density by lazy {
        context.resources.displayMetrics.density
    }

    private val Int.dp get() = this * density

    private val border = Rect()

    private val defaultStrokeWidth = 1.dp

    private val expandedHintColor = R.color.colorPrimaryDark
    private val gapPadding = 5.dp
    private val gapStart = 16.dp

    private val hint by lazy {
        editText?.run {
            if (hint != null) {
                val newHint = hint
                hint = ""
                newHint.toString()
            } else {
                ""
            }
        } ?: ""
    }

    private val textSize: Float by lazy {
        editText?.run {
            textSize
        } ?: 0f
    }

    private val cutPadding get() = 8.dp / sqrt(2f)

    private val borderPaint by lazy {
        Paint().apply {
            color = ContextCompat.getColor(context, R.color.shadowColor)
            style = Paint.Style.STROKE
            strokeWidth = defaultStrokeWidth
        }
    }

    private val hintPaint by lazy {
        Paint().apply {
            editText?.let {
                color = it.currentHintTextColor
                textSize = it.textSize
            }
        }
    }

    private val backgroundColor by lazy {
        (background as ColorDrawable).color
    }

    private val hintBounds = Rect()

    private val editText: EditText? by lazy {
        (getChildAt(0) as? EditText)?.apply {
            setOnTouchListener(this@OutlinedEditText)
            onFocusChangeListener = this@OutlinedEditText
            this.setPadding(
                    16.dp.i,
                    16.dp.i,
                    16.dp.i,
                    16.dp.i
            )
            background = resetBorder()
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            this.maxLines = 1
            setSingleLine()
        }
    }

    private var hintOffset = 1f
    private var hintSizeOffset = 1f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defaultAttributeStyle: Int) : super(context, attributeSet, defaultAttributeStyle) {
        init()
    }

    private fun init() {
        setPadding(
                paddingLeft,
                paddingTop + 16.dp.i,
                paddingRight,
                paddingBottom
        )
        orientation = LinearLayout.HORIZONTAL
    }

    private fun resetBorder(): Drawable {
        return ShapeDrawable().apply {
            shape = CutCornersShape(8.dp)
            paint.color = borderPaint.color
            paint.style = borderPaint.style
            paint.strokeWidth = borderPaint.strokeWidth
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        editText?.let {
            invalidate()
            border.set(it.left, it.top, it.right, it.bottom)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.let {
            hintPaint.textSize = textSize * hintSizeOffset
            hintPaint.getTextBounds(hint, 0, hint.length, hintBounds)
            val position = defineHintPosition()
            val bottom = position.bottomOfHint
            val left = position.leftOfHint
            canvas.drawPath(Path().apply {
                moveTo(left - gapPadding, bottom)
                lineTo(left - gapPadding, bottom)
                lineTo(left - gapPadding, bottom - hintBounds.height())
                lineTo(left + hintBounds.width() / 2, bottom - position.heightOfTriangle)
                lineTo(left + gapPadding + hintBounds.width(), bottom - hintBounds.height())
                lineTo(left + gapPadding + hintBounds.width(), bottom)
            }, Paint().apply {
                color = backgroundColor
                style = Paint.Style.FILL
            })
            canvas.drawText(hint, left, bottom, hintPaint)
        }
    }

    private fun defineHintPosition(): Positions {
        val gravity = editText?.gravity ?: Gravity.CENTER or Gravity.START
        val verticalDistance = when (gravity) {
            Gravity.TOP,
            Gravity.TOP or Gravity.START,
            Gravity.TOP or Gravity.END,
            Gravity.TOP or Gravity.CENTER_HORIZONTAL ->
                (editText?.paddingTop ?: 0) + hintBounds.height() * 5 / 4f

            Gravity.CENTER_VERTICAL,
            Gravity.CENTER_VERTICAL or Gravity.START,
            Gravity.CENTER_VERTICAL or Gravity.END,
            Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL ->
                border.height().f / 2 + hintBounds.height() / 2

            Gravity.BOTTOM,
            Gravity.BOTTOM or Gravity.START,
            Gravity.BOTTOM or Gravity.END,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL ->
                border.height().f - (editText?.paddingBottom ?: 0)

            else -> border.height().f / 2 + hintBounds.height() / 2
        }

        val horizontalDistance = when (gravity) {
            Gravity.START,
            Gravity.START or Gravity.TOP,
            Gravity.START or Gravity.BOTTOM,
            Gravity.START or Gravity.CENTER_VERTICAL ->
                gapStart + borderPaint.strokeWidth / 2 + cutPadding

            Gravity.CENTER_HORIZONTAL,
            Gravity.CENTER_HORIZONTAL or Gravity.TOP,
            Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,
            Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL ->
                border.width().f / 2 - hintBounds.width() / 2

            Gravity.END,
            Gravity.END or Gravity.TOP,
            Gravity.END or Gravity.BOTTOM,
            Gravity.END or Gravity.CENTER_VERTICAL ->
                border.width().f - (gapStart + borderPaint.strokeWidth / 2 + cutPadding) - hintBounds.width()

            else -> gapStart + borderPaint.strokeWidth / 2 + cutPadding
        }


        val bottomOfHint = border.top + verticalDistance * hintOffset + (1 - hintOffset) * hintBounds.height() / 2
        val heightOfTriangle = verticalDistance - borderPaint.strokeWidth
        val leftOfHint = border.left + horizontalDistance
        return Positions(
                bottomOfHint = bottomOfHint,
                heightOfTriangle = heightOfTriangle,
                leftOfHint = leftOfHint
        )
    }

    private data class Positions(val bottomOfHint: Float,
                                 val heightOfTriangle: Float,
                                 val leftOfHint: Float)

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            if (editText?.hasFocus() != true) {
                editText?.requestFocus()
            }
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        onHasFocus(hasFocus)
    }

    private fun onHasFocus(hasFocus: Boolean) {
        borderPaint.color = if (!hasFocus) {
            ContextCompat.getColor(context, R.color.shadowColor)
        } else {
            ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }

        editText?.let {
            it.background = resetBorder()
        }

        if (editText?.text?.isNotEmpty() == true) {
            return
        }

        val animator = AnimatorSet().apply {
            duration = 300
        }

        val hintOffsetStart = if (hasFocus) 1f else 0f
        val hintOffsetEnd = if (hasFocus) 0f else 1f
        val hintTextSizeOffsetStart = if (hasFocus) 1f else 0.8f
        val hintTextSizeOffsetEnd = if (hasFocus) 0.8f else 1f
        val hintColorStart = if (hasFocus) editText?.currentHintTextColor else ContextCompat.getColor(context, expandedHintColor)
        val hintColorEnd = if (hasFocus) ContextCompat.getColor(context, expandedHintColor) else editText?.currentHintTextColor

        animator += ValueAnimator.ofFloat(hintOffsetStart, hintOffsetEnd).apply {
            addUpdateListener {
                this@OutlinedEditText.hintOffset = it.animatedValue as Float
                invalidate()
            }
        }

        animator += ValueAnimator.ofFloat(hintTextSizeOffsetStart, hintTextSizeOffsetEnd).apply {
            addUpdateListener {
                this@OutlinedEditText.hintSizeOffset = it.animatedValue as Float
                invalidate()
            }
        }

        animator += ObjectAnimator.ofObject(hintPaint, "color", ArgbEvaluator(),
                hintColorStart, hintColorEnd)

        animator.addListener(
                onStart = {
                    editText?.isCursorVisible = false
                },
                onEnd = {
                    editText?.isCursorVisible = true
                }
        )

        animator.start()
    }
}