package com.example.namigtahmazli.shrineappnative

import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.Gravity
import android.view.View

enum class Sides {
    Left, Top, Right, Bottom
}

fun View.dropShadow(
        elevation: Float = context.dimension(R.dimen.elevation),
        cornerRadius: Float = context.dimension(R.dimen.cornerRadius),
        shadowColor: Int = context.color(R.color.shadowColor),
        backgroundColor: Int = context.color(android.R.color.background_light),
        mShape: Shape,
        elevationGravity: Int = Gravity.CENTER,
        elevationSides: Array<Sides> = arrayOf(Sides.Left, Sides.Top, Sides.Right, Sides.Bottom),
        ripple: Boolean = false,
        rippleColor: Int = context.color(R.color.colorAccent)
): Drawable {

    val elevationPadding = Rect(0, 0, 0, 0)
    elevationSides.forEach {
        when (it) {
            Sides.Left -> elevationPadding.left = elevation.toInt()
            Sides.Right -> elevationPadding.right = elevation.toInt()
            Sides.Top -> elevationPadding.top = elevation.toInt()
            Sides.Bottom -> elevationPadding.bottom = elevation.toInt()
        }
    }

    val dy = when (elevationGravity) {
        Gravity.CENTER -> 0f
        Gravity.TOP -> {
            elevationPadding.top += elevation.toInt()
            -elevation.toInt() / 3f
        }
        Gravity.BOTTOM -> {
            elevationPadding.bottom += elevation.toInt()
            elevation.toInt() / 3f
        }
        else -> 0f
    }

    val shapeDrawable = ShapeDrawable().apply {
        setPadding(elevationPadding)
        paint.color = backgroundColor
        paint.setShadowLayer(cornerRadius / 3, 0f, dy, shadowColor)
        shape = mShape
    }

    setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

    val drawables = mutableListOf<Drawable>().apply {
        add(shapeDrawable)
        if (ripple) {
            add(RippleDrawable(
                    ColorStateList.valueOf(rippleColor),
                    null,
                    shapeDrawable
            ))
        }
    }
    return LayerDrawable(drawables.toTypedArray()).apply {
        setLayerInset(0,
                elevationPadding.left,
                elevationPadding.top,
                elevationPadding.right,
                elevationPadding.bottom
        )
    }
}