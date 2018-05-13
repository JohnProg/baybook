package com.kitobim.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.kitobim.Constants.IS_ACTIVE
import com.kitobim.Constants.THEME
import com.kitobim.Constants.THEME_LIGHT
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.get
import com.kitobim.R
import com.kitobim.fragment.MainFragment
import com.kitobim.fragment.WelcomeFragment


class MainActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var sContext: Context
        lateinit var sWindow: Window
        lateinit var sFragmentManager: FragmentManager

        fun setStatusBarColor(isDarkTheme: Boolean) {

            val currentFragment = sFragmentManager.findFragmentById(R.id.fragment_container)
            val fragmentTransaction = sFragmentManager.beginTransaction()
            fragmentTransaction.detach(currentFragment)
            fragmentTransaction.attach(currentFragment)
            fragmentTransaction.commit()

            val decor = sWindow.decorView

            if (isDarkTheme) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    sWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    sWindow.statusBarColor = ContextCompat.getColor(sContext, R.color.toolbar_dark)
                    decor.systemUiVisibility = 0
                }

            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    sWindow.statusBarColor = ContextCompat.getColor(sContext, R.color.toolbar)
                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    sWindow.statusBarColor = ContextCompat.getColor(sContext, R.color.grey200)
                }
            }
        }
    }

    private var mFragment: Fragment? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        sContext = this
        sWindow = window
        sFragmentManager = supportFragmentManager

        val prefs = PreferenceHelper.defaultPrefs(this)
        val isActive: Boolean = prefs[IS_ACTIVE, false]
        val theme =
                if (prefs[THEME, THEME_LIGHT] == THEME_LIGHT) R.style.AppTheme_Light
                else R.style.AppTheme_Dark

        setTheme(theme)
        setContentView(R.layout.activity_main)

        mFragment =
                if (isActive) MainFragment.newInstance()
                else WelcomeFragment.newInstance()

        replaceFragment()
    }

    private fun replaceFragment() {
        if (mFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, mFragment)
            transaction.commit()
        }
    }
}





