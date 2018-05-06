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
            R.id.btn_theme_color -> ThemeColorFragment.newInstance()
            R.id.btn_language -> LanguageFragment.newInstance()
            R.id.btn_help -> HelpFragment.newInstance()
            R.id.btn_about_us -> AboutUsFragment.newInstance()
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

}
