package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.data.local.preference.PreferenceHelper.set
import com.kitobim.ui.activity.AuthenticationActivity
import com.kitobim.util.Constants
import com.kitobim.util.Constants.IS_ACTIVE
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
        val username = mPreference[Constants.USERNAME, ""]
        val emailOrPhone = mPreference[Constants.EMAIL_OR_PHONE, ""]

        mView.txt_username_profile.text = username
        mView.txt_email_phone_profile.text = emailOrPhone

        if (isActive) {
            mView.nav_view_profile.visibility = View.VISIBLE
            mView.txt_username_profile.visibility = View.VISIBLE
            mView.txt_email_phone_profile.visibility = View.VISIBLE
            mView.txt_profile_empty.visibility = View.GONE
            mView.btn_profile_empty.visibility = View.GONE
        } else {
            mView.nav_view_profile.visibility = View.GONE
            mView.txt_username_profile.visibility = View.GONE
            mView.txt_email_phone_profile.visibility = View.GONE
            mView.txt_profile_empty.visibility = View.VISIBLE
            mView.btn_profile_empty.visibility = View.VISIBLE
        }

        mView.btn_profile_empty.setOnClickListener { logout() }
        mView.nav_view_profile.setNavigationItemSelectedListener(this)

        return mView
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mFragment = when (item.itemId) {
            R.id.nav_payment -> PaymentFragment.newInstance()
            R.id.nav_notification -> NotificationFragment.newInstance()
            R.id.nav_feedback -> FeedbackFragment.newInstance()
            R.id.nav_logout -> { showLogoutDialog();  null }
            else -> null
        }
        changeFragment {
            add(R.id.fragment_container_full, mFragment).addToBackStack(null)
        }

        return true
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(getString(R.string.dialog_logout_title))
        builder.setMessage(getString(R.string.dialog_loguot_message))

        val positiveText = getString(R.string.logout)
        builder.setPositiveButton(positiveText,
                { dialog, _ ->
                    logout()
                    dialog.dismiss()
                })

        val negativeText = getString(R.string.cancel)
        builder.setNegativeButton(negativeText,
                { dialog, _ ->
                    dialog.dismiss()
                })

        val dialog = builder.create()
        dialog.show()
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