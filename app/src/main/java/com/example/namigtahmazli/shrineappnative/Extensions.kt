package com.example.namigtahmazli.shrineappnative

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.content.res.TypedArray
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

val View.density get() = context.resources.displayMetrics.density
val Context.density get() = resources.displayMetrics.density

fun Context.color(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

fun Context.dimension(@DimenRes res: Int): Float {
    return resources.getDimension(res)
}

val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

operator fun AnimatorSet.plusAssign(animator: Animator) {
    play(animator)
}

inline fun Animator.addListener(crossinline onStart: (Animator?) -> Unit = {},
                                crossinline onEnd: (Animator?) -> Unit = {}) {
    val listener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            onEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            onStart(animation)
        }
    }
    addListener(listener)
}

fun ImageView.loadImage(url: String) {
    val options = RequestOptions.skipMemoryCacheOf(true)
    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
}

inline fun TypedArray.use(block: TypedArray.() -> Unit) {
    try {
        block()
    } finally {
        recycle()
    }
}