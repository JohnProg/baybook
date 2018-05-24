package com.kitobim.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.adapter.BookAdapter
import com.kitobim.data.model.Book
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_store.view.*


class StoreFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener{

    companion object {
        fun newInstance(): StoreFragment = StoreFragment()
    }

    private lateinit var mService: ApiService
    private lateinit  var mAdapter: BookAdapter
    private var bookList = mutableListOf<Book>()
    private var isGridLayout: Boolean = false

    // vars for fetching pages
    private var page = 1
    private var hasNext = false
    private var visibleItemCount = 0
    private var lastVisibleItemPosition = 0
    private var totalItemCount = 0

    private lateinit var parent: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        parent = inflater.inflate(R.layout.fragment_store, container, false)

        mService = RetrofitClient.getAuthService(context!!)

        initToolbar()
        initRecyclerView()
        updateBookList()

        return parent
    }

    private fun initToolbar() {

    }

    private fun initRecyclerView() {

        val linearLayout = LinearLayoutManager(activity)
        val gridLayout = GridLayoutManager(activity, 3)

        mAdapter = BookAdapter(activity!!, bookList, isGridLayout)

        parent.recycler_view.apply {
            adapter = mAdapter
            setHasFixedSize(true)

            layoutManager = if (isGridLayout) gridLayout else linearLayout

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount

                    lastVisibleItemPosition = if (isGridLayout) {
                        gridLayout.findLastVisibleItemPosition()
                    } else {
                        linearLayout.findLastVisibleItemPosition()
                    }

                    // todo check how is method working (find proper solution)
                    if (hasNext && lastVisibleItemPosition + visibleItemCount*2 >= page * 15) {
                        page ++
                        updateBookList()
                    }
                }
            })
        }
    }

    private fun updateBookList(){

        mService.getBooks(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess ->
                        Log.i("tag", "onSuccess")
                        val list = onSuccess.data
                        mAdapter.addAllAndUpdate(list)
                        hasNext = onSuccess.meta.current_page < onSuccess.meta.last_page

                    }, {
                        onFailure -> Log.i("tag", "Failure ${onFailure.message}")
                    }
                )
    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

}