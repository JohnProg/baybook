package com.kitobim.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_change_password.view.*

class ChangePasswordFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener{

    companion object {
        fun newInstance(): Fragment = ChangePasswordFragment()
    }

    private val TOOLBAR_NAVIGATION_ID = -1
    private lateinit var mView: View
    private var mFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false)

        mView.fab_send_request.setOnClickListener(this)
        mView.toolbar_change_password.setNavigationOnClickListener(this)

        return mView
    }

    override fun onClick(v: View) {
        when (v.id) {
            TOOLBAR_NAVIGATION_ID -> activity!!.onBackPressed()
        }
    }

}
