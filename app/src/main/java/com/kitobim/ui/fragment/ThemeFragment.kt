package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.data.local.preference.PreferenceHelper.set
import com.kitobim.ui.activity.MainActivity
import com.kitobim.util.Constants.THEME
import com.kitobim.util.Constants.THEME_DARK
import com.kitobim.util.Constants.THEME_LIGHT
import kotlinx.android.synthetic.main.fragment_theme.*
import kotlinx.android.synthetic.main.fragment_theme.view.*


class ThemeFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener {

    companion object {
        fun newInstance(): Fragment = ThemeFragment()
    }

    private lateinit var mView: View
    private lateinit var mPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mPreferences = PreferenceHelper.defaultPrefs(context!!)

        mView = inflater.inflate(R.layout.fragment_theme, container, false)

        mView.btn_theme_light.setOnClickListener(this)
        mView.btn_theme_dark.setOnClickListener(this)

        setHasOptionsMenu(true)
        return mView
    }
    
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(toolbar_theme)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        val oldTheme = mPreferences[THEME, THEME_LIGHT]

        val newTheme = when (v.id) {
            R.id.btn_theme_light -> {
                mPreferences[THEME] = THEME_LIGHT
                THEME_LIGHT
            } R.id.btn_theme_dark -> {
                mPreferences[THEME] = THEME_DARK
                THEME_DARK
            } else -> THEME_LIGHT
        }

        if (oldTheme != newTheme) {
            activity!!.overridePendingTransition(0, 0)
            activity!!.finish()
            activity!!.overridePendingTransition(0, 0)

            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

}
