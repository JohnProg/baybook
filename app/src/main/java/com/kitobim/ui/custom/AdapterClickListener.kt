package com.kitobim.ui.custom

import android.widget.ImageView

interface AdapterClickListener{
    fun onItemClick(holder: ImageView, position: Int)
}