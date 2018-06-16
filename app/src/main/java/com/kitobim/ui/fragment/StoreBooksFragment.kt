package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.kitobim.R
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.ui.adapter.BookAdapter
import com.kitobim.ui.custom.RecyclerItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_store_books.view.*




class StoreBooksFragment @SuppressLint("ValidFragment") private constructor() : Fragment() {

    companion object {
        fun newInstance(): Fragment = StoreBooksFragment()
    }

    private lateinit var mView: View

    private lateinit var mAdapter: BookAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mService: ApiService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_store_books, container, false)
        mService = RetrofitClient.getAuthService(context!!)

        initViews()

        setHasOptionsMenu(true)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments!!.getString("title")
        val type = arguments!!.getString("type")
        val id = arguments!!.getInt("id")

        mView.toolbar_store_books.title = title
        val handler = Handler()
        handler.postDelayed({
            loadBooks(type, id)
        }, 300)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(mView.toolbar_store_books)
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


    private fun initViews() {
        mAdapter = BookAdapter(context!!)
        mLinearLayoutManager = LinearLayoutManager(context)


        mView.rv_store_books.apply {
            setHasFixedSize(true)
            layoutManager = mLinearLayoutManager
            adapter = mAdapter
        }


        mView.rv_store_books.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, mView.rv_store_books,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                selectBook(position)
                            }

                            override fun onLongItemClick(view: View?, position: Int) {
                                selectBook(position)
                            }
                        })
        )
    }

    private fun loadBooks(type: String, id: Int) {
        Log.i("tag", "type: $type id: $id")
        when (type) {
            "author" -> {
                mService.getAuthorBooks(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val books = onSuccess.data
                                    mAdapter.updateData(books)
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure store books ${onFailure.message}")
                                }
                        )
            }
            "genre" -> {
                mService.getGenreBooks(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val books = onSuccess.data
                                    mAdapter.updateData(books)
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure store books ${onFailure.message}")
                                }
                        )
            }
            "publisher" -> {
                mService.getPublisherBooks(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val books = onSuccess.data
                                    mAdapter.updateData(books)
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure store books ${onFailure.message}")
                                }
                        )
            }
            "collection" -> {
                mService.getCollectionBooks(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val books = onSuccess.data
                                    mAdapter.updateData(books)
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure store books ${onFailure.message}")
                                }
                        )
            }
        }
    }


    private fun selectBook(position: Int) {
        val fragment = BookInfoFragment.newInstance()
        val book = mAdapter.getItem(position)
        val bundle = Bundle()

        bundle.putInt("id", book.id)
        bundle.putString("title", book.title)
        fragment.arguments = bundle

        fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                        R.anim.enter_from_right, R.anim.exit_to_right)
                .add(R.id.fragment_container_full, fragment)
                .addToBackStack(null)
                .commit()
    }

}