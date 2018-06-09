package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.ui.custom.BottomNavigationViewHelper
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment  @SuppressLint("ValidFragment") private constructor()
    : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {


    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    private lateinit var mView: View
    private var mFragment: Fragment? = null
    private val url = "http://development.baysoftware.ru/storage/thumbnails/0000910025-L.jpg"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_main, container, false)

        initViews()

        return mView
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
            replace(R.id.fragment_container_child, mFragment)
        }

        return true
    }

    private fun initViews(){
        BottomNavigationViewHelper.disableShiftMode(mView.bottom_navigation)
        mView.bottom_navigation.setOnNavigationItemSelectedListener(this)
        mView.bottom_navigation.selectedItemId = R.id.nav_library

        ImageHelper.setBookCover(mView.recent_book, url)
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = childFragmentManager.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }
}