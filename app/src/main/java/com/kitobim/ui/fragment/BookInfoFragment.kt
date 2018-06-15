package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.repository.BookRepository
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.fragment_book_info.*
import kotlinx.android.synthetic.main.fragment_book_info.view.*


class BookInfoFragment @SuppressLint("ValidFragment") private constructor(): Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): Fragment = BookInfoFragment()
    }

    private lateinit var mView: View
    private var mBookId: Int = 0
    private lateinit var mBookRepo: BookRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_book_info, container, false)

        mBookId = arguments!!.getInt("book_id")
        initViews()

        mBookRepo = BookRepository.getInstance(activity!!.application)

        return mView
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(toolbar_book_info)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_purchase_book_info -> mBookRepo.downloadBook(mBookId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        mView.btn_purchase_book_info.setOnClickListener(this)
        setHasOptionsMenu(true)
    }
    @SuppressLint("SetTextI18n")
    private fun updateUi(book: BookEntity) {
        if (book.thumbnail != null) ImageHelper.setBookCover(mView.cover_book_info, book.thumbnail)

        if (book.price == 0) mView.price_book_info.text = getString(R.string.free)
        else mView.price_book_info.text = "Price: ${book.price} ${getString(R.string.sum)}"

        mView.title_book_info.text = book.title
        mView.author_book_info.text = TextUtils.join(", ", book.authors.map { it.name })
    }



    private fun downloadCover() {

    }
}