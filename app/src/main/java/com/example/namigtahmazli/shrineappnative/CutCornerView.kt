package com.example.namigtahmazli.shrineappnative

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.Gravity

class CutCornerView : ConstraintLayout {

    private val path: Path = Path()
    private lateinit var shapePath: Path
    private var defaultCut = 16 * density
    private var defaultBackgroundColor = context.color(android.R.color.background_light)
    private val offset: Offset = Offset()

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defaultAttributeStyle: Int) : super(context, attributeSet, defaultAttributeStyle) {
        val corners = mutableListOf<Corner>()
        val shadowSides = mutableListOf<Sides>()
        var elevationGravity = Gravity.CENTER

        val typedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.CutCornerView, defaultAttributeStyle, 0)
        val topLeft = typedArray.getBoolean(R.styleable.CutCornerView_topLeft, true)
        val topRight = typedArray.getBoolean(R.styleable.CutCornerView_topRight, true)
        val bottomLeft = typedArray.getBoolean(R.styleable.CutCornerView_bottomLeft, true)
        val bottomRight = typedArray.getBoolean(R.styleable.CutCornerView_bottomRight, true)
        val topShadow = typedArray.getBoolean(R.styleable.CutCornerView_topShadow, true)
        val leftShadow = typedArray.getBoolean(R.styleable.CutCornerView_leftShadow, true)
        val rightShadow = typedArray.getBoolean(R.styleable.CutCornerView_rightShadow, true)
        val bottomShadow = typedArray.getBoolean(R.styleable.CutCornerView_bottomShadow, true)
        defaultCut = typedArray.getDimension(R.styleable.CutCornerView_cut, defaultCut)
        defaultBackgroundColor = typedArray.getColor(R.styleable.CutCornerView_backgroundColor, defaultBackgroundColor)
        elevationGravity = typedArray.getInteger(R.styleable.CutCornerView_elevationGravity, elevationGravity)

        typedArray.recycle()

        if (leftShadow) {
            offset.dx = context.dimension(R.dimen.elevation)
        }
        if (topShadow) {
            offset.dy = context.dimension(R.dimen.elevation)
        }
        corners.apply {
            if (topLeft) {
                add(Corner.TopLeft)
            }
            if (topRight) {
                add(Corner.TopRight)
            }
            if (bottomLeft) {
                add(Corner.BottomLeft)
            }
            if (bottomRight) {
                add(Corner.BottomRight)
            }
        }

        shadowSides.apply {
            if (topShadow) {
                add(Sides.Top)
            }
            if (leftShadow) {
                add(Sides.Left)
            }
            if (rightShadow) {
                add(Sides.Right)
            }
            if (bottomShadow) {
                add(Sides.Bottom)
            }
        }

        val shape = CutCornersShape(
                cut = defaultCut,
                corners = corners.toTypedArray(),
                strokePadding = false
        )

        shapePath = shape.path

        background = dropShadow(
                mShape = shape,
                backgroundColor = defaultBackgroundColor,
                elevationSides = shadowSides.toTypedArray(),
                elevationGravity = elevationGravity
        )

        clipChildren = true
    }

    override fun onDraw(canvas: Canvas?) {
        path.set(shapePath)
        path.offset(offset.dx, offset.dy)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }

    data class Offset(var dx: Float = 0f,
                      var dy: Float = 0f)
}