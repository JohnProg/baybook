package com.kitobim.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.LocaleHelper
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_language.*
import kotlinx.android.synthetic.main.fragment_language.view.*


class LanguageFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): Fragment = LanguageFragment()
    }

    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_language, container, false)

        mView.nav_view_language.setNavigationItemSelectedListener(this)
        mView.nav_view_language.itemIconTintList = null

        setHasOptionsMenu(true)
        return mView
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(toolbar_language)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val language = when (item.itemId) {
            R.id.btn_lang_uz -> "uz"
            R.id.btn_lang_ru -> "ru"
            R.id.btn_lang_en -> "en"
            R.id.btn_lang_tr -> "tr"
            else -> LocaleHelper.getLanguage(context!!)
        }

        for (i in 0..3) {
            mView.nav_view_language.menu.getItem(i).actionView = null
        }

        item.setActionView(R.layout.image_selected_row)
        return true
    }

}
