package com.kitobim.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): Fragment = ProfileFragment()
    }

    private lateinit var mView: View
    private var mFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)

        mView.nav_view_profile.setNavigationItemSelectedListener(this)
        return mView
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mFragment = when (item.itemId) {
            R.id.btn_account -> AccountFragment.newInstance()
            R.id.btn_payment -> PaymentFragment.newInstance()
            R.id.btn_notification -> NotificationFragment.newInstance()
            R.id.btn_feedback -> FeedbackFragment.newInstance()
            R.id.btn_logout -> {
                logout()
                null
            }
            else -> null
        }

        if (mFragment != null) {
            parentFragment!!.fragmentManager!!.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .addToBackStack(null)
                    .commit()
        }

        return true
    }

    private fun logout() {
        // todo logout logic
    }
}