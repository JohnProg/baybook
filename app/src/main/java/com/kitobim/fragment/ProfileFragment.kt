package com.kitobim.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.Constants
import com.kitobim.Constants.IS_ACTIVE
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.get
import com.kitobim.PreferenceHelper.set
import com.kitobim.R
import com.kitobim.activity.AuthenticationActivity
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): Fragment = ProfileFragment()
    }

    private lateinit var mView: View
    private lateinit var mPreference: SharedPreferences
    private var mFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)
        mPreference = PreferenceHelper.defaultPrefs(context!!)
        val isActive = mPreference[IS_ACTIVE, false]

        if (!isActive) {
            mView.nav_view_profile.visibility = View.GONE
            mView.txt_profile_empty.visibility = View.VISIBLE
            mView.btn_profile_empty.visibility = View.VISIBLE
        } else {
            mView.nav_view_profile.visibility = View.VISIBLE
            mView.txt_profile_empty.visibility = View.GONE
            mView.btn_profile_empty.visibility = View.GONE
        }

        mView.btn_profile_empty.setOnClickListener { logout() }
        mView.nav_view_profile.setNavigationItemSelectedListener(this)

        return mView
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mFragment = when (item.itemId) {
            R.id.btn_account -> AccountFragment.newInstance()
            R.id.btn_payment -> PaymentFragment.newInstance()
            R.id.btn_notification -> NotificationFragment.newInstance()
            R.id.btn_feedback -> FeedbackFragment.newInstance()
            R.id.btn_logout -> { logout();  null }
            else -> null
        }
        changeFragment {
            add(R.id.fragment_container, mFragment).addToBackStack(null)
        }

        return true
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = parentFragment!!.fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }

    private fun logout() {
        mPreference[IS_ACTIVE] = false
        mPreference[Constants.USERNAME] = ""
        mPreference[Constants.PASSWORD] = ""
        mPreference[Constants.TOKEN] = ""
        val intent = Intent(activity, AuthenticationActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
}