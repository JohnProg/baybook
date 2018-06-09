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
import com.kitobim.ui.adapter.*
import com.kitobim.ui.custom.EndlessScrollListener
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
        mBookAdapter.clearData()
        mAuthorAdapter.clearData()
        mScrollListener.resetState()
        when (id) {
            R.id.nav_authors -> {
                mView.rv_inner_store.adapter = mAuthorAdapter

                mStoreViewModel.apply {
                    getAllAuthors().observe(this@StoreInnerFragment, Observer {
                        mAuthorAdapter.updateAuthors(it!!)
                    })
                    insertAllAuthors(1)
                }
            }
            R.id.nav_genres -> {
                mView.rv_inner_store.adapter = mGenreAdapter

                mStoreViewModel.apply {
                    getAllGenres().observe(this@StoreInnerFragment, Observer {
                        mGenreAdapter.updateGenres(it!!)
                    })
                    insertAllGenres(1)
                }
            }
            R.id.nav_publishers -> {
                mView.rv_inner_store.adapter = mPublisherAdapter

                mStoreViewModel.apply {
                    getAllPublishers().observe(this@StoreInnerFragment, Observer {
                        mPublisherAdapter.updatePublishers(it!!)
                    })
                    insertAllPublishers()
                }
            }
            R.id.nav_collections -> {
                mView.rv_inner_store.adapter = mCollectionAdapter

                mStoreViewModel.apply {
                    getAllCollections().observe(this@StoreInnerFragment, Observer {
                        mCollectionAdapter.updateCollections(it!!)
                    })
                    insertAllCollections()
                }
            }
            R.id.nav_wishlist -> {
                mView.rv_inner_store.adapter = mBookAdapter

                mStoreViewModel.apply {
                    getAllWishlist().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateBooks(it!!)
                    })
                    insertAllWishlist()
                }
            }
            R.id.nav_recommended_books -> {
                mView.rv_inner_store.adapter = mBookAdapter


                mStoreViewModel.apply {
                    getAllRecommendedBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateBooks(it!!)
                    })
                    insertAllRecommendedBooks()
                }
            }
            R.id.nav_new_books -> {
                mView.rv_inner_store.adapter = mBookAdapter

                mStoreViewModel.apply {
                    getAllNewBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateBooks(it!!)
                    })
                    insertAllNewBooks(1)
                }
            }
            R.id.nav_free_books -> {
                mView.rv_inner_store.adapter = mBookAdapter

                mStoreViewModel.apply {
                    getAllFreeBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateBooks(it!!)
                    })
                    insertAllFreeBooks()
                }
            }
            R.id.nav_paid_books -> {
                mView.rv_inner_store.adapter = mBookAdapter

                mStoreViewModel.apply {
                    getAllPaidBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateBooks(it!!)
                    })
                    insertAllPaidBooks()
                }
            }
            R.id.nav_rated_books -> {
                mView.rv_inner_store.adapter = mBookAdapter

                mStoreViewModel.apply {
                    getAllRatedBooks().observe(this@StoreInnerFragment, Observer {
                        mBookAdapter.updateBooks(it!!)
                    })
                    insertAllRatedBooks()
                }
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
                if (page > 1) loadPage(page)
            }
        }

        mView.rv_inner_store.apply {
            hasFixedSize()
            layoutManager = mLinearLayoutManager
            addOnScrollListener(mScrollListener)
        }


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

        val navId = arguments!!.getInt("nav_id")
        changeDirection(navId)
    }

    private fun loadPage(page: Int) {
        when (mCurrentPageId) {
            R.id.nav_authors -> mStoreViewModel.insertAllAuthors(page)
            R.id.nav_genres -> mStoreViewModel.insertAllGenres(page)
            R.id.nav_new_books -> mStoreViewModel.insertAllNewBooks(page)
        }
    }

}