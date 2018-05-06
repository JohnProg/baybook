package com.kitobim

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.squareup.picasso.Picasso

class CoverView : ImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setImageUrl(url: String) {
        Picasso.get().load(url).fit().into(this)
    }
}