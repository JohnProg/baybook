package com.kitobim.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.BottomNavigationViewHelper
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    private val mUrl = "http://kitobim.uz/media/books/2018/01/27/Tahorat_kitobi1-500x750.jpg.100x133_q85_crop.jpg"
    private lateinit var mView: View
    private var mFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_main, container, false)

        BottomNavigationViewHelper.disableShiftMode(mView.bottom_navigation)
        mView.bottom_navigation.setOnNavigationItemSelectedListener(this)
        mView.bottom_navigation.selectedItemId = R.id.btn_library

        mView.recent_book.setImageUrl(mUrl)
        return mView
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        mFragment = when (item.itemId) {
            R.id.btn_library -> LibraryFragment.newInstance()
            R.id.btn_store -> StoreFragment.newInstance()
            R.id.btn_settings -> SettingsFragment.newInstance()
            R.id.btn_profile -> ProfileFragment.newInstance()
            else -> null
        }

        if (mFragment != null) {
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_child, mFragment)
            transaction.commit()
        }

        return true
    }
}
