package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.ui.activity.MainActivity
import com.kitobim.util.LocaleHelper
import kotlinx.android.synthetic.main.fragment_language.*
import kotlinx.android.synthetic.main.fragment_language.view.*


class LanguageFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): Fragment = LanguageFragment()
    }

    private lateinit var mView: View
    private lateinit var mLanguage: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_language, container, false)

        mView.nav_view_language.setNavigationItemSelectedListener(this)
        mView.nav_view_language.itemIconTintList = null

        mLanguage = LocaleHelper.getLanguage(context!!)
        val index = when (mLanguage) {
            "uz" -> 0
            "ru" -> 1
            "en" -> 2
            "tr" -> 3
            else -> -1
        }

        if (index != -1) {
            mView.nav_view_language.menu.getItem(index).setActionView(R.layout.image_selected_row)
        }

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

        val newLanguage = when (item.itemId) {
            R.id.btn_lang_uz -> "uz"
            R.id.btn_lang_ru -> "ru"
            R.id.btn_lang_en -> "en"
            R.id.btn_lang_tr -> "tr"
            else -> mLanguage
        }

        if (mLanguage != newLanguage) {
            LocaleHelper.persistLanguage(context!!, newLanguage)

            activity!!.overridePendingTransition(0, 0)
            activity!!.finish()
            activity!!.overridePendingTransition(0, 0)

            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        return true
    }

}
