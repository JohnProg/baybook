package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.ui.adapter.*
import com.kitobim.ui.custom.EndlessScrollListener
import com.kitobim.ui.custom.RecyclerItemClickListener
import com.kitobim.util.Constants
import com.kitobim.viewmodel.StoreViewModel
import kotlinx.android.synthetic.main.fragment_store_inner.*
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

        setHasOptionsMenu(true)
        return mView
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(toolbar_store_inner)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            } else -> super.onOptionsItemSelected(item)
        }
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
                mView.toolbar_store_inner.setTitle(R.string.authors)
                mStoreViewModel.loadAllAuthors().observe(this, Observer {
                    mAuthorAdapter.updateData(it!!)
                })
                loadPage(1)
            }
            R.id.nav_genres -> {
                mView.toolbar_store_inner.setTitle(R.string.genres)
                mStoreViewModel.loadAllGenres().observe(this, Observer {
                    mGenreAdapter.updateData(it!!)
                })
                loadPage(1)
            }
            R.id.nav_new_books -> {
                mView.toolbar_store_inner.setTitle(R.string.new_released_books)
                mStoreViewModel.loadAllNewBooks().observe(this, Observer {
                    mBookAdapter.updateData(it!!)
                })
                loadPage(1)
            }
            R.id.nav_publishers -> {
                mView.toolbar_store_inner.setTitle(R.string.publishers)
                mStoreViewModel.loadAllPublishers().observe(this@StoreInnerFragment, Observer {
                    mPublisherAdapter.updateData(it!!)
                })
            }
            R.id.nav_collections -> {
                mView.toolbar_store_inner.setTitle(R.string.collections)
                mStoreViewModel.loadAllCollections().observe(this@StoreInnerFragment, Observer {
                    mCollectionAdapter.updateData(it!!)
                })
            }
            R.id.nav_wishlist -> {
                mView.toolbar_store_inner.setTitle(R.string.wishlist)
                mStoreViewModel.loadAllWishlist().observe(this@StoreInnerFragment, Observer {
                    mBookAdapter.updateData(it!!)
                })
            }
            R.id.nav_recommended_books -> {
                mView.toolbar_store_inner.setTitle(R.string.recommended_books)
                mStoreViewModel.loadRecommendedBooks().observe(this@StoreInnerFragment, Observer {
                    mBookAdapter.updateData(it!!)
                })
            }
            R.id.nav_paid_books -> {
                mView.toolbar_store_inner.setTitle(R.string.top_paid_books)
                mStoreViewModel.loadAllPaidBooks().observe(this@StoreInnerFragment, Observer {
                    mBookAdapter.updateData(it!!)
                })
            }
            R.id.nav_rated_books -> {
                mView.toolbar_store_inner.setTitle(R.string.top_rated_books)
                mStoreViewModel.loadAllRatedBooks().observe(this@StoreInnerFragment, Observer {
                    mBookAdapter.updateData(it!!)
                })
            }
            R.id.nav_free_books -> {
                mView.toolbar_store_inner.setTitle(R.string.top_free_books)
                mStoreViewModel.loadAllFreeBooks().observe(this@StoreInnerFragment, Observer {
                    mBookAdapter.updateData(it!!)
                })
            }
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

        mView.rv_inner_store.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, mView.rv_inner_store,
                        object : RecyclerItemClickListener.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        val book = mBookAdapter.getItem(position)
                        Log.i("tag", "Item click pos: $position and title: ${book.title}")
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        val book = mBookAdapter.getItem(position)
                        Log.i("tag", "Item long click pos: $position and title: ${book.title}")
                    }
                })
        )
        mView.rv_inner_store.apply {
            hasFixedSize()
            layoutManager = mLinearLayoutManager
            addOnScrollListener(mScrollListener)
        }

        val navId = arguments!!.getInt("nav_id")
        changeDirection(navId)
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