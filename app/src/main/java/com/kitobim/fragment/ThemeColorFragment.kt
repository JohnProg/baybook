package com.kitobim.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.Constants.THEME
import com.kitobim.Constants.THEME_DARK
import com.kitobim.Constants.THEME_LIGHT
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.get
import com.kitobim.PreferenceHelper.set
import com.kitobim.R
import com.kitobim.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_theme.*
import kotlinx.android.synthetic.main.fragment_theme.view.*


class ThemeColorFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener {

    companion object {
        fun newInstance(): Fragment = ThemeColorFragment()
    }

    private lateinit var mView: View
    private lateinit var mPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mPreferences = PreferenceHelper.defaultPrefs(context!!)

//        todo try changing theme without recreating activity or open current fragment
        val theme =
                if (mPreferences[THEME, THEME_LIGHT] == THEME_LIGHT) R.style.AppTheme_Light
                else R.style.AppTheme_Dark

        context!!.theme.applyStyle(theme, true)
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        val contextThemeWrapper = ContextThemeWrapper(activity, theme)

        // clone the inflater using the ContextThemeWrapper
        val localInflater = inflater.cloneInContext(contextThemeWrapper)

        mView = localInflater.inflate(R.layout.fragment_theme, container, false)

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
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_theme_light -> mPreferences[THEME] = THEME_LIGHT
            R.id.btn_theme_dark -> mPreferences[THEME] = THEME_DARK
        }

        MainActivity.setStatusBarColor(v.id == R.id.btn_theme_dark)

//        activity!!.overridePendingTransition(0, 0)
//        activity!!.finish()
//        activity!!.overridePendingTransition(0, 0)
//
//        val intent = Intent(activity, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//        intent.putExtra("bundle", "theme_changed")
//        startActivity(intent)
    }

}
