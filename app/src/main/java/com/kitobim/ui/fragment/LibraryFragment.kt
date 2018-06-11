package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.repository.LibraryRepository
import com.kitobim.ui.adapter.BookAdapter
import com.kitobim.viewmodel.LibraryViewModel
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.android.synthetic.main.fragment_library.view.*

class LibraryFragment @SuppressLint("ValidFragment") private constructor()
        : Fragment(), View.OnClickListener {


    companion object {
        fun newInstance(): Fragment = LibraryFragment()
    }

    private lateinit var mView: View
    private lateinit var mAdapter: BookAdapter
    private lateinit var mRepository: LibraryRepository
    private lateinit var mLibraryViewModel: LibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_library, container, false)

        initToolbar()
        initRecyclerView()
        mLibraryViewModel = ViewModelProviders.of(activity!!).get(LibraryViewModel::class.java)

        mLibraryViewModel.getAllBooks().observe(this, Observer {
            mAdapter.updateData(it!!)
            if (mAdapter.itemCount > 0) {
                mView.btn_go_to_store.visibility = View.GONE
                mView.txt_library_empty.visibility = View.GONE
                mView.recycler_view_library.visibility = View.VISIBLE
            }
        })

        return mView
    }

    override fun onClick(v: View) {
        when (v) {
            btn_go_to_store -> {
                val fragment = StoreFragment.newInstance()

                fragmentManager!!
                        .beginTransaction()
                        .replace(R.id.fragment_container_child, fragment)
                        .commit()
            }
        }
    }

    private fun initRecyclerView() {
        mAdapter = BookAdapter(context!!)
        mAdapter.setItemClickListener(object : BookAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {

            }
        })

        mView.recycler_view_library.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context!!)
        }
    }

    private fun initToolbar() {
        mRepository = LibraryRepository.getInstance(activity!!.application)
        mView.btn_add_library.setOnClickListener { }
    }
}
