package com.kitobim.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient


class BookInfoFragment @SuppressLint("ValidFragment") private constructor(): Fragment() {

    companion object {
        fun newInstance(): Fragment = BookInfoFragment()
    }

    private lateinit var parent: View
    private lateinit var mService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        parent = inflater.inflate(R.layout.fragment_book_info, container, false)

        mService = RetrofitClient.getAuthService(context!!)

        return parent
    }



}