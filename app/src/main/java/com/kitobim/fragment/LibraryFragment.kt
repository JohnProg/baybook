package com.kitobim.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.android.synthetic.main.fragment_library.view.*

class LibraryFragment @SuppressLint("ValidFragment") private constructor()
        : Fragment(), View.OnClickListener {


    companion object {
        fun newInstance(): Fragment = LibraryFragment()
    }

    private lateinit var parent: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        parent = inflater.inflate(R.layout.fragment_library, container, false)

        parent.btn_library_empty.setOnClickListener(this)
        return parent
    }

    override fun onClick(v: View) {
        when (v) {

            btn_library_empty -> {
                val fragment = StoreFragment.newInstance()

                fragmentManager!!
                        .beginTransaction()
                        .replace(R.id.fragment_container_child, fragment)
                        .commit()
            }
        }
    }
}
