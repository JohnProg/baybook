package com.kitobim.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.kitobim.Constants.IS_ACTIVE
import com.kitobim.Constants.THEME
import com.kitobim.Constants.THEME_LIGHT
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.get
import com.kitobim.R
import com.kitobim.fragment.LoginFragment
import com.kitobim.fragment.MainFragment


class MainActivity : AppCompatActivity() {

    private var mFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceHelper.defaultPrefs(this)
        val isActive: Boolean = prefs[IS_ACTIVE, false]
        val theme =
                if (prefs[THEME, THEME_LIGHT] == THEME_LIGHT) R.style.AppTheme_Light
                else R.style.AppTheme_Dark

        setTheme(theme)
        setContentView(R.layout.activity_main)

        mFragment =
                if (isActive) MainFragment.newInstance()
                else LoginFragment.newInstance()

        if (mFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, mFragment)
            transaction.commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            currentFragment?.onResume()
        }
    }

    override fun onResume() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else super.onResume()
    }

}





