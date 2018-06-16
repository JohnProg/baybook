package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.data.local.database.entity.AuthorEntity
import com.kitobim.repository.AuthorRepository
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.fragment_author_info.view.*


class AuthorInfoFragment @SuppressLint("ValidFragment") private constructor(): Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): Fragment = AuthorInfoFragment()
    }

    private lateinit var mView: View
    private var mAuthorId: Int = 0
    private var mAuthorName: String = ""
    private lateinit var mBookRepo: AuthorRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_author_info, container, false)
        mAuthorId = arguments!!.getInt("id")
        mAuthorName = arguments!!.getString("title")

        initViews()

        mBookRepo = AuthorRepository.getInstance(activity!!.application)
        mBookRepo.getAuthorById(mAuthorId).observe(this, Observer {
            if (it != null) updateUi(it)
        })

        return mView
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(mView.toolbar_author_info)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_books_author_info -> {
                val fragment = StoreBooksFragment.newInstance()
                val bundle = Bundle()
                bundle.putString("title", mAuthorName)
                bundle.putString("type", "author")
                bundle.putInt("id", mAuthorId)

                fragment.arguments = bundle


                fragmentManager!!.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_right)
                        .add(R.id.fragment_container_full, fragment)
                        .addToBackStack(null)
                        .commit()
            }
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
        mView.toolbar_author_info.title = mAuthorName
        mView.btn_books_author_info.setOnClickListener(this)
        setHasOptionsMenu(true)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(author: AuthorEntity) {
        mAuthorName = author.name

        if (author.books_count > 0) {
            mView.books_count_author_info.text = "${getString(R.string.available_books)}: ${author.books_count} "
            mView.btn_books_author_info.text = getText(R.string.books)
        } else {
            mView.books_count_author_info.text = getString(R.string.no_books)
            mView.btn_books_author_info.visibility = View.INVISIBLE
        }

        mView.title_author_info.text = author.name

        if (author.thumbnail != null) ImageHelper.setAuthorImage(   mView.cover_author_info, author.thumbnail)
    }



    private fun downloadCover() {

    }
}