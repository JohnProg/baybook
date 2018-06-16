package com.kitobim.ui.custom

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.transition.ChangeBounds
import android.support.transition.ChangeImageTransform
import android.support.transition.ChangeTransform
import android.support.transition.TransitionSet
import android.util.AttributeSet


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class DetailsTransition : TransitionSet {

    constructor() {
        init()
    }
    /**
     *
     * This constructor allows us to use this transition in XML
     *
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(ChangeImageTransform())
    }
}