package com.kitobim.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.kitobim.R
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.ui.custom.BottomNavigationViewHelper
import com.kitobim.ui.custom.ImageHelper
import com.kitobim.ui.fragment.LibraryFragment
import com.kitobim.ui.fragment.ProfileFragment
import com.kitobim.ui.fragment.SettingsFragment
import com.kitobim.ui.fragment.StoreFragment
import com.kitobim.util.Constants
import com.kitobim.util.Constants.THEME
import com.kitobim.util.Constants.THEME_LIGHT
import com.kitobim.util.LocaleHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_store.*




class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var mFragment: Fragment? = null
    private val url = "http://development.baysoftware.ru/storage/thumbnails/0000910025-L.jpg"

    private lateinit var mPreference: SharedPreferences

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPreference = PreferenceHelper.defaultPrefs(this)
        val isNewbie = mPreference[Constants.IS_NEWBIE, true]
        val isActive = mPreference[Constants.IS_ACTIVE, false]
        if (!isActive || isNewbie) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }

        val theme = if (mPreference[THEME, THEME_LIGHT] == THEME_LIGHT) {
            R.style.AppTheme_Light
        } else {
            R.style.AppTheme_Dark
        }
        setTheme(theme)
        setContentView(R.layout.activity_main)

        initViews()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mFragment = when (item.itemId) {
            R.id.nav_library -> LibraryFragment.newInstance()
            R.id.nav_store -> StoreFragment.newInstance()
            R.id.nav_settings -> SettingsFragment.newInstance()
            R.id.nav_profile -> ProfileFragment.newInstance()
            else -> null
        }

        changeFragment {
            replace(R.id.fragment_container_inner, mFragment)
        }

        return true
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

    fun goToStore() {
        bottom_navigation.selectedItemId = R.id.nav_store
    }


    private fun initViews(){
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.nav_library

        ImageHelper.setBookCover(recent_book, url)
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }
}





