package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.kitobim.R
import com.kitobim.data.local.database.entity.AuthorEntity
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.GenreEntity
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.repository.FrontStoreRepository
import com.kitobim.ui.adapter.AuthorAdapter
import com.kitobim.ui.adapter.BookAdapter
import com.kitobim.ui.adapter.GenreAdapter
import com.kitobim.ui.custom.FrontStoreListener
import com.kitobim.ui.custom.RecyclerItemClickListener
import com.kitobim.util.Constants.THEME
import com.kitobim.util.Constants.THEME_LIGHT
import kotlinx.android.synthetic.main.fragment_store.view.*
import kotlinx.android.synthetic.main.layout_main_content_store.view.*
import kotlinx.android.synthetic.main.toolbar_store.view.*


class StoreFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(): StoreFragment = StoreFragment()
    }

    private lateinit var mView: View
    private lateinit var mBookAdapter1: BookAdapter
    private lateinit var mBookAdapter2: BookAdapter
    private lateinit var mBookAdapter3: BookAdapter
    private lateinit var mAuthorAdapter: AuthorAdapter
    private lateinit var mGenreAdapter: GenreAdapter

    private val mStoreFragment = StoreInnerFragment.newInstance()
    private val mBookFragment = BookInfoFragment.newInstance()
    private var mLastQuery = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_store, container, false)

        initToolbar()
        initRecyclerViews()
        initSearchBar()
        updateUi()

        return mView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_pinned_books -> changeNavigation(R.id.nav_recommended_books )
            R.id.btn_most_authors -> changeNavigation(R.id.nav_authors)
            R.id.btn_most_genres ->  changeNavigation(R.id.nav_genres)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        changeNavigation(item.itemId)
        mView.drawer_layout_store.closeDrawer(Gravity.START)
        return true
    }

    private fun updateUi() {
        FrontStoreRepository.getInstance(activity!!.application).apply {

            loadPinnedBooks()
            loadAuthors()
            loadTopMonthBooks()
            loadGenres()
            loadRandomBooks()

            setStoreListener(object: FrontStoreListener{
                override fun setPinnedBooks(list: List<BookEntity>) {
                    mBookAdapter1.updateData(list)
                }

                override fun setTopMonthBooks(list: List<BookEntity>) {
                    mBookAdapter2.updateData(list)
                }

                override fun setRandomBooks(list: List<BookEntity>) {
                    mBookAdapter3.updateData(list)
                }

                override fun setAuthors(list: List<AuthorEntity>) {
                    mAuthorAdapter.updateData(list)
                }

                override fun setGenres(list: List<GenreEntity>) {
                    mGenreAdapter.updateData(list)
                }
            })
        }

    }
    @SuppressLint("ResourceType")
    private fun initToolbar() {
        val prefs = PreferenceHelper.defaultPrefs(context!!)
        val theme = prefs[THEME, THEME_LIGHT]
        val scrimColor: String
        scrimColor = if (theme == THEME_LIGHT) {
            getString(R.color.scrim)
        } else {
            getString(R.color.scrim_dark)
        }

        mView.drawer_layout_store.setScrimColor(Color.parseColor(scrimColor))
        mView.nav_view_store.setNavigationItemSelectedListener(this)
        getStoreButtons().forEach { it.setOnClickListener(this) }
        setHasOptionsMenu(true)
    }

    private fun initRecyclerViews() {
        mAuthorAdapter = AuthorAdapter(context!!,true)
        mGenreAdapter = GenreAdapter(context!!, true)
        mBookAdapter1 = BookAdapter(context!!, true)
        mBookAdapter2 = BookAdapter(context!!, true)
        mBookAdapter3 = BookAdapter(context!!, true)

        getRecyclerViews().forEach {
            it.apply {
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
            }

            when(it.id) {
                R.id.rv_pinned_books -> it.adapter = mBookAdapter1
                R.id.rv_most_authors -> it.adapter = mAuthorAdapter
                R.id.rv_month_books -> it.adapter = mBookAdapter2
                R.id.rv_most_genres -> it.adapter = mGenreAdapter
                R.id.rv_random_books -> it.adapter = mBookAdapter3
            }
        }

        mView.rv_pinned_books.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, mView.rv_pinned_books,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val book = mBookAdapter1.getItem(position)
                                selectBook(book)
                            }

                            override fun onLongItemClick(view: View?, position: Int) {
                                val book = mBookAdapter1.getItem(position)
                                selectBook(book)
                            }
                        })
        )

        mView.rv_most_authors.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, mView.rv_most_authors,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val author = mAuthorAdapter.getItem(position)
                                selectAuthor(author)
                            }
                            override fun onLongItemClick(view: View?, position: Int) {
                                val author = mAuthorAdapter.getItem(position)
                                selectAuthor(author)
                            }
                        })
        )

        mView.rv_month_books.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, mView.rv_month_books,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val book = mBookAdapter2.getItem(position)
                                selectBook(book)
                            }
                            override fun onLongItemClick(view: View?, position: Int) {
                                val book = mBookAdapter2.getItem(position)
                                selectBook(book)
                            }
                        })
        )

        mView.rv_most_genres.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, mView.rv_most_genres,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val genre = mGenreAdapter.getItem(position)
                                selectGenre(genre)
                            }
                            override fun onLongItemClick(view: View?, position: Int) {
                                val genre = mGenreAdapter.getItem(position)
                                selectGenre(genre)
                            }
                        })
        )

        mView.rv_random_books.addOnItemTouchListener(
                RecyclerItemClickListener(context!!, mView.rv_random_books,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val book = mBookAdapter3.getItem(position)
                                selectBook(book)
                            }
                            override fun onLongItemClick(view: View?, position: Int) {
                                val book = mBookAdapter3.getItem(position)
                                selectBook(book)
                            }
                        })
        )
    }

    private fun initSearchBar() {
        mView.floating_search_view.attachNavigationDrawerToMenuButton(mView.drawer_layout_store)

        mView.floating_search_view.setOnQueryChangeListener { oldQuery, newQuery ->
            mLastQuery = newQuery
            if (oldQuery != "" && newQuery == "") {
                mView.floating_search_view.clearSuggestions()
                mView.floating_search_view.hideProgress()
            } else {
                //this shows the top left circular progress
                //you can call it where ever you want, but
                //it makes sense to do it when loading something in
                //the background.
                mView.floating_search_view.showProgress()
            }
            Log.d("tag", "onSearchTextChanged()")
        }

        mView.floating_search_view.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
                Log.d("tag", "onSuggestionClicked()")
            }

            override fun onSearchAction(query: String) {
                Log.d("tag", "onSearchAction()")
            }
        })

        mView.floating_search_view.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocus() {
                //show suggestions when search bar gains focus (typically history suggestions)
                mView.floating_search_view.setSearchText(mLastQuery)
                Log.d("tag", "onFocus()")
            }

            override fun onFocusCleared() {
                //set the title of the bar so that when focus is returned a new query begins
                mView.floating_search_view.hideProgress()
                mView.floating_search_view.setSearchBarTitle(getString(R.string.kitobim_store))
                Log.d("tag", "onFocusCleared()")
            }
        })

    }

    private fun changeNavigation(id: Int) {
        if (!mStoreFragment.isVisible) {
            val bundle = Bundle()
            bundle.putInt("nav_id", id)
            mStoreFragment.arguments = bundle
            changeFragment {
                add(R.id.fragment_container_full, mStoreFragment).addToBackStack(null)
            }
        }
    }

    private fun selectBook(book: BookEntity) {
        val bundle = Bundle()
        bundle.putInt("id", book.id)
        bundle.putString("title", book.title)

        mBookFragment.arguments = bundle

        fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                        R.anim.enter_from_right, R.anim.exit_to_right)
                .add(R.id.fragment_container_full, mBookFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun selectAuthor(author: AuthorEntity) {
        val bundle = Bundle()
        val fragment = AuthorInfoFragment.newInstance()
        bundle.putInt("id", author.id)
        bundle.putString("title", author.name)

        fragment.arguments = bundle

        fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                        R.anim.enter_from_right, R.anim.exit_to_right)
                .add(R.id.fragment_container_full, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun selectGenre(genre: GenreEntity) {
        val fragment = StoreBooksFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("title", genre.name)
        bundle.putString("type", "genre")
        bundle.putInt("id", genre.id)

        fragment.arguments = bundle


        fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                        R.anim.enter_from_right, R.anim.exit_to_right)
                .add(R.id.fragment_container_full, fragment)
                .addToBackStack(null)
                .commit()
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right)
        transaction.code()
        transaction.commit()
    }

    private fun getRecyclerViews() = arrayOf(mView.rv_pinned_books, mView.rv_most_authors,
            mView.rv_month_books, mView.rv_most_genres, mView.rv_random_books)

    private fun getStoreButtons() = arrayOf(mView.btn_pinned_books, mView.btn_most_authors, mView.btn_most_genres)

}