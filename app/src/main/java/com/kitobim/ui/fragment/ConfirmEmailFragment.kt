package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_confirm_email.view.*

class ConfirmEmailFragment @SuppressLint("ValidFragment") private constructor() : Fragment() {

    companion object {
        fun newInstance() =  ConfirmEmailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_confirm_email, container, false)
        val email = arguments!!.getString("email")

        view.subtitle_confirm_email.text = getString(R.string.subtitle_confirm_email, email)
        view.fab_confirm_email.setOnClickListener{
            val fragment = LoginPagerFragment.newInstance()
            fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_container_auth, fragment).addToBackStack("main")
                    .commit()
        }

        return view
    }
}
