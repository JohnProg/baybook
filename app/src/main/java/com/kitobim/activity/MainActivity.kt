package com.kitobim.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.kitobim.Constants.IS_ACTIVE
import com.kitobim.Constants.IS_NEWBIE
import com.kitobim.Constants.THEME
import com.kitobim.Constants.THEME_LIGHT
import com.kitobim.LocaleHelper
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.get
import com.kitobim.R
import com.kitobim.fragment.MainFragment




class MainActivity : AppCompatActivity() {

    private var mFragment: Fragment? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceHelper.defaultPrefs(this)
        val theme = if (prefs[THEME, THEME_LIGHT] == THEME_LIGHT) {
            R.style.AppTheme_Light
        } else {
            R.style.AppTheme_Dark
        }

        setTheme(theme)
        setContentView(R.layout.activity_main)

        val isNewbie = prefs[IS_NEWBIE, true]
        val isActive = prefs[IS_ACTIVE, false]

        if (!isActive || isNewbie) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if (savedInstanceState == null) {
            mFragment = MainFragment.newInstance()
            changeFragment{
                replace(R.id.fragment_container, mFragment)
            }
        }

    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }
}





