package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.ui.adapter.BookAdapter
import kotlinx.android.synthetic.main.fragment_store_inner.view.*

class StoreInnerFragment @SuppressLint("ValidFragment") private constructor() : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): Fragment = StoreInnerFragment()
    }

    private lateinit var mView: View
    private lateinit var mAdapter: BookAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_store_inner, container, false)

        initViews()
        return mView
    }

    override fun onClick(v: View?) {

    }

    private fun initViews() {
        mAdapter = BookAdapter(context!!)
       mView.rv_inner_store.apply {
           hasFixedSize()
           adapter
       }
    }


}