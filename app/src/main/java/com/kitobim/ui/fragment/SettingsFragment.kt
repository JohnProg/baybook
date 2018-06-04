package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): Fragment = SettingsFragment()
    }

    private lateinit var mView: View
    private var mFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_settings, container, false)

        mView.nav_view_settings.setNavigationItemSelectedListener(this)
        return mView
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mFragment = when (item.itemId) {
            R.id.nav_theme -> ThemeFragment.newInstance()
            R.id.nav_language -> LanguageFragment.newInstance()
            R.id.nav_help -> HelpFragment.newInstance()
            R.id.nav_about_us -> AboutUsFragment.newInstance()
            else -> null
        }

        changeFragment {
            add(R.id.fragment_container, mFragment).addToBackStack(null)
        }

        return true
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction =  parentFragment!!.fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }
}
