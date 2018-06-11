package com.kitobim.ui.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewAnimationUtils
import com.kitobim.R

object CircularRevealAnimation {


    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun circleReveal(context: Context, view: View, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean) {
        var width = view.width

        if (posFromRight > 0) {
            width -= posFromRight * context.resources
                    .getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - context.resources
                    .getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2
        }
        if (containsOverflow) {
            width -=  context.resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)
        }

        val cx = width
        val cy = view.height / 2
        val anim: Animator

        anim = if (isShow) ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, width.toFloat())
        else ViewAnimationUtils.createCircularReveal(view, cx, cy, width.toFloat(), 0f)

        anim.duration = 220.toLong()

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            }
        })
        // make the view visible and start the animation
        if (isShow) view.visibility = View.VISIBLE

        // start the animation
        anim.start()
    }
}