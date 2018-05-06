package com.kitobim.fragment

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.FastBlur
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_book_info.view.*


class BookInfoFragment @SuppressLint("ValidFragment") private constructor(): Fragment() {

    companion object {
        fun newInstance(): Fragment = BookInfoFragment()
    }

    private lateinit var parent: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        parent = inflater.inflate(R.layout.fragment_book_info, container, false)

        val blurredBitmap = FastBlur.blur(context!!, BitmapFactory.decodeResource(resources, R.drawable.bird))
        parent.image_bg_book_info.setImageBitmap(blurredBitmap)
        return parent
    }



}