package com.example.namigtahmazli.shrineappnative

import android.graphics.*
import android.graphics.drawable.shapes.Shape

enum class Corner {
    TopLeft, TopRight, BottomLeft, BottomRight
}

class CutCornersShape(val cut: Float,
                      val corners: Array<Corner> = arrayOf(
                              Corner.TopLeft,
                              Corner.TopRight,
                              Corner.BottomLeft,
                              Corner.BottomRight
                      ),
                      val strokePadding: Boolean = true) : Shape() {
    private var mRect = RectF()
    val path = Path()
    override fun draw(canvas: Canvas?, paint: Paint?) {
        canvas?.drawPath(drawOutlines(paint?.strokeWidth ?: 0f), paint)
    }

    private fun drawOutlines(lineHeight: Float): Path {
        val mRect = if (strokePadding) {
            RectF(
                    this.mRect.left + lineHeight / 2,
                    this.mRect.top + lineHeight / 2,
                    this.mRect.right - lineHeight / 2,
                    this.mRect.bottom - lineHeight / 2
            )
        } else {
            this.mRect
        }

        return path.apply {
            if (corners.contains(Corner.TopLeft)) {
                moveTo(mRect.left + cut, mRect.top)
            } else {
                moveTo(mRect.left, mRect.top)
            }

            if (corners.contains(Corner.TopRight)) {
                lineTo(mRect.right - cut, mRect.top)
                lineTo(mRect.right, mRect.top + cut)
            } else {
                lineTo(mRect.right, mRect.top)
            }

            if (corners.contains(Corner.BottomRight)) {
                lineTo(mRect.right, mRect.bottom - cut)
                lineTo(mRect.right - cut, mRect.bottom)
            } else {
                lineTo(mRect.right, mRect.bottom)
            }

            if (corners.contains(Corner.BottomLeft)) {
                lineTo(mRect.left + cut, mRect.bottom)
                lineTo(mRect.left, mRect.bottom - cut)
            } else {
                lineTo(mRect.left, mRect.bottom)
            }

            if (corners.contains(Corner.TopLeft)) {
                lineTo(mRect.left, mRect.top + cut)
                lineTo(mRect.left + cut, mRect.top)
            } else {
                lineTo(mRect.left, mRect.top)
            }
        }
    }

    override fun getOutline(outline: Outline) {
        val rect = Rect()
        outline.setRect(Math.ceil(rect.left.toDouble()).toInt(), Math.ceil(rect.top.toDouble()).toInt(),
                Math.floor(rect.right.toDouble()).toInt(), Math.floor(rect.bottom.toDouble()).toInt())
    }

    override fun onResize(width: Float, height: Float) {
        super.onResize(width, height)
        mRect = RectF(0f, 0f, width, height)
    }

    override fun clone(): Shape {
        return (super.clone() as CutCornersShape).apply {
            mRect = RectF()
        }
    }
}