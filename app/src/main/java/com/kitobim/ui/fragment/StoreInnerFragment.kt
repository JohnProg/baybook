package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.ui.adapter.*
import com.kitobim.ui.custom.EndlessScrollListener
import com.kitobim.util.Constants
import com.kitobim.viewmodel.StoreViewModel
import kotlinx.android.synthetic.main.fragment_store_inner.view.*


class StoreInnerFragment @SuppressLint("ValidFragment") private constructor() : Fragment() {

    companion object {
        fun newInstance(): Fragment = StoreInnerFragment()
    }

    private lateinit var mView: View
    private lateinit var mStoreViewModel: StoreViewModel
    private lateinit var mScrollListener: EndlessScrollListener

    private lateinit var mBookAdapter: BookAdapter
    private lateinit var mGenreAdapter: GenreAdapter
    private lateinit var mAuthorAdapter: AuthorAdapter
    private lateinit var mPublisherAdapter: PublisherAdapter
    private lateinit var mCollectionAdapter: CollectionAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private var mCurrentPageId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_store_inner, container, false)

        mStoreViewModel= ViewModelProviders.of(this).get(StoreViewModel::class.java)
        initViews()

        return mView
    }

    fun changeDirection(id: Int) {
        mCurrentPageId = id
        mScrollListener.resetState()
        mAuthorAdapter.clearData()
        mGenreAdapter.clearData()
        mBookAdapter.clearData()

        when (id) {
            R.id.nav_authors -> mView.rv_inner_store.adapter = mAuthorAdapter
            R.id.nav_genres -> mView.rv_inner_store.adapter = mGenreAdapter
            R.id.nav_publishers -> mView.rv_inner_store.adapter = mPublisherAdapter
            R.id.nav_collections -> mView.rv_inner_store.adapter = mCollectionAdapter

            R.id.nav_wishlist,
            R.id.nav_recommended_books,
            R.id.nav_new_books,
            R.id.nav_paid_books,
            R.id.nav_rated_books,
            R.id.nav_free_books -> mView.rv_inner_store.adapter = mBookAdapter
        }

        when (id) {
            R.id.nav_authors -> {
                mStoreViewModel.loadAllAuthors().observe(this, Observer {
                    mAuthorAdapter.updateData(it!!)
                })
                loadPage(1)
            }
            R.id.nav_genres -> {
                mStoreViewModel.loadAllGenres().observe(this, Observer {
                    mGenreAdapter.updateData(it!!)
                })
                loadPage(1)
            }
            R.id.nav_new_books -> {
                mStoreViewModel.loadAllNewBooks().observe(this, Observer {
                    mBookAdapter.updateData(it!!)
                })
                loadPage(1)
            }
            R.id.nav_publishers ->
                mStoreViewModel.loadAllPublishers().observe(this@StoreInnerFragment, Observer {
                        mPublisherAdapter.updateData(it!!)
                    })
            R.id.nav_collections ->
                mStoreViewModel.loadAllCollections().observe(this@StoreInnerFragment, Observer {
                        mCollectionAdapter.updateData(it!!)
                    })
            R.id.nav_wishlist ->
                mStoreViewModel.loadAllWishlist().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateData(it!!)
                    })
            R.id.nav_recommended_books ->
                mStoreViewModel.loadRecommendedBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateData(it!!)
                    })
            R.id.nav_paid_books ->
                mStoreViewModel.loadAllPaidBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateData(it!!)
                    })
            R.id.nav_rated_books ->
                mStoreViewModel.loadAllRatedBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateData(it!!)
                    })
            R.id.nav_free_books ->
                mStoreViewModel.loadAllFreeBooks().observe(this@StoreInnerFragment, Observer {
                    mBookAdapter.updateData(it!!)
                })
        }
    }

    private fun initViews() {
        mBookAdapter = BookAdapter(context!!)
        mAuthorAdapter = AuthorAdapter(context!!)
        mGenreAdapter = GenreAdapter(context!!)
        mPublisherAdapter = PublisherAdapter()
        mCollectionAdapter = CollectionAdapter()

        mLinearLayoutManager = LinearLayoutManager(context)

        mScrollListener = object : EndlessScrollListener(mLinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                loadPage(totalItemsCount)
            }
        }

        mView.rv_inner_store.apply {
            hasFixedSize()
            layoutManager = mLinearLayoutManager
            addOnScrollListener(mScrollListener)
        }

        val navId = arguments!!.getInt("nav_id")
        changeDirection(navId)

        mBookAdapter.setItemClickListener(object : BookAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {

            }
        })

        mAuthorAdapter.setItemClickListener(object : AuthorAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {

            }
        })

        mGenreAdapter.setItemClickListener(object : GenreAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {

            }
        })

        mPublisherAdapter.setItemClickListener(object : PublisherAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {

            }
        })
        mCollectionAdapter.setItemClickListener(object : CollectionAdapter.OnItemClickListener{
            override fun onItemClick(id: Int) {

            }
        })
    }

    private fun loadPage(totalItemCount: Int) {
        when (mCurrentPageId) {
            R.id.nav_authors -> {
                val page = totalItemCount / Constants.AUTHOR_PAGE_THRESHOLD + 1
                Log.i("tag", "Authors nextPage: $page  totalItemCount: $totalItemCount")
                mStoreViewModel.loadAuthorsByPage(page)
            }
            R.id.nav_genres -> {
                val page = totalItemCount / Constants.GENRE_PAGE_THRESHOLD + 1
                Log.i("tag", "Genre nextPage page: $page  totalItemCount: $totalItemCount")
                mStoreViewModel.loadGenresByPage(page)
            }
            R.id.nav_new_books -> {
                val page = totalItemCount / Constants.BOOK_PAGE_THRESHOLD + 1
                Log.i("tag", "NewBooks nextPage: $page  totalItemCount: $totalItemCount")
                mStoreViewModel.loadNewBooksByPage(page)
            }
        }
    }

}