package com.kitobim.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import com.kitobim.R
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.ui.fragment.MainFragment
import com.kitobim.util.Constants
import com.kitobim.util.Constants.THEME
import com.kitobim.util.Constants.THEME_LIGHT
import com.kitobim.util.LocaleHelper
import kotlinx.android.synthetic.main.fragment_store.*




class MainActivity : AppCompatActivity() {

    private lateinit var mPreference: SharedPreferences

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPreference = PreferenceHelper.defaultPrefs(this)

        initTheme()
        setContentView(R.layout.activity_main)
        initViews(savedInstanceState)
    }

    override fun onBackPressed() {
        val layout = drawer_layout_store
        if (layout != null && layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }


    private fun initTheme() {
        val theme = if (mPreference[THEME, THEME_LIGHT] == THEME_LIGHT) {
            R.style.AppTheme_Light
        } else {
            R.style.AppTheme_Dark
        }
        setTheme(theme)
    }

    private fun initViews(bundle: Bundle?){
        val isNewbie = mPreference[Constants.IS_NEWBIE, true]
        val isActive = mPreference[Constants.IS_ACTIVE, false]

        if (!isActive || isNewbie) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        } else if (bundle == null) {
            val fragment = MainFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment).commit()
        }
    }
}





