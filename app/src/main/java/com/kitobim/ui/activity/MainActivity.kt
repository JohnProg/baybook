package com.kitobim.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import com.kitobim.R
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.repository.BookRepository
import com.kitobim.ui.fragment.MainFragment
import com.kitobim.util.Constants.IS_ACTIVE
import com.kitobim.util.Constants.IS_NEWBIE
import com.kitobim.util.Constants.THEME
import com.kitobim.util.Constants.THEME_LIGHT
import com.kitobim.util.LocaleHelper
import kotlinx.android.synthetic.main.fragment_store.*


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

        val url = "http://development.baysoftware.ru/storage/covers/XSp5ntQO7s8pxyv6O9V4X5qnykbQ8DuoUnBO0EaV.jpeg"
        val book = BookEntity(id = 40001,
                title =  "White Fang",
                thumbnail = url,
                authors = "Jack London",
                price = 5000)

        val repo = BookRepository.getInstance(application)
        repo.insert(book)

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
}





