package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_welcome.view.*


class WelcomeFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener {

    companion object {
        fun newInstance(): Fragment = WelcomeFragment()
    }

    private lateinit var mView: View
    private var mFragment: Fragment? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_welcome, container, false)

        mView.btn_login_welcome.setOnClickListener(this)
        mView.btn_register_welcome.setOnClickListener(this)
        return mView
    }

    override fun onClick(v: View) {
        mFragment = when (v.id){
            R.id.btn_login_welcome -> LoginPagerFragment.newInstance()
            R.id.btn_register_welcome -> RegisterFragment.newInstance()
            else -> null
        }
        changeFragment {
            replace(R.id.fragment_container_auth, mFragment,"login").addToBackStack(null)
        }
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }
}
