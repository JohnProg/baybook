package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.data.model.Book
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_book_info.view.*


class BookInfoFragment @SuppressLint("ValidFragment") private constructor(): Fragment() {

    companion object {
        fun newInstance(): Fragment = BookInfoFragment()
    }

    private lateinit var mView: View
    private lateinit var mService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_book_info, container, false)

        mService = RetrofitClient.getAuthService(context!!)
        val id = arguments!!.getInt("id")
        Log.i("tag", "id: $id")
        val call = mService.getBook(id)

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess ->
                    Log.i("tag", "onSuccess")
                    initViews(onSuccess)
                },
                {
                    onFailure -> Log.i("tag", "Failure ${onFailure.message}")
                })

        return mView
    }

    private fun initViews(book: Book) {
        if (book.cover != null) mView.cover_book_info.setImageUrl(book.cover)

        if (book.price == 0) mView.price_book_info.text = getString(R.string.free)
        else mView.price_book_info.text = "Price: ${book.price} ${getString(R.string.sum)}"

        mView.title_book_info.text = book.title
        mView.author_book_info.text = TextUtils.join(", ", book.authors)

    }
}