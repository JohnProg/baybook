package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.ui.custom.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    private val mUrl = "http://development.baysoftware.ru/storage/covers/XSp5ntQO7s8pxyv6O9V4X5qnykbQ8DuoUnBO0EaV.jpeg"
    private lateinit var mView: View
    private var mFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_main, container, false)


        BottomNavigationViewHelper.disableShiftMode(mView.bottom_navigation)
        mView.bottom_navigation.setOnNavigationItemSelectedListener(this)
        mView.bottom_navigation.selectedItemId = R.id.nav_library

        mView.recent_book.setImageUrl(mUrl)
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

        replaceFragment()
        return true
    }

    private fun replaceFragment(){
        if (mFragment != null) {
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_child, mFragment)
            transaction.commit()
        }
    }
}