package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.kitobim.ui.custom.CircularRevealAnimation.circleReveal
import com.kitobim.ui.custom.FrontStoreListener
import com.kitobim.util.Constants.THEME
import com.kitobim.util.Constants.THEME_LIGHT
import kotlinx.android.synthetic.main.fragment_store.view.*
import kotlinx.android.synthetic.main.toolbar_search_store.view.*
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

    private var mFragment: Fragment = StoreInnerFragment.newInstance()
    private lateinit var mSearchItem: MenuItem
    private var mCurrentNavId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_store, container, false)

        initToolbar()
        initSearchToolbar()
        initRecyclerViews()
        updateUi()

        return mView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_pinned_books -> mView.nav_view_store.menu.findItem(R.id.nav_recommended_books).isChecked = true
            R.id.btn_most_authors -> mView.nav_view_store.menu.findItem(R.id.nav_authors).isChecked = true
            R.id.btn_most_genres ->  mView.nav_view_store.menu.findItem(R.id.nav_genres).isChecked = true
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (mCurrentNavId != item.itemId) {
            mCurrentNavId = item.itemId
            mView.nav_view_store.setCheckedItem(item.itemId)

            if (!mFragment.isVisible) {
                val bundle = Bundle()
                bundle.putInt("nav_id", mCurrentNavId)
                mFragment.arguments = bundle
                changeFragment {
                    replace(R.id.fragment_container_store, mFragment).addToBackStack(null)
                }
            } else {
                (mFragment as StoreInnerFragment).changeDirection(mCurrentNavId)
            }
            mView.toolbar_store.title = item.title
        }

        if (mView.drawer_layout_store.isDrawerOpen(Gravity.START)){
            mView.drawer_layout_store.closeDrawer(Gravity.START)
        }
        return true

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_store_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val prefs = PreferenceHelper.defaultPrefs(context!!)
        val theme = prefs[THEME, THEME_LIGHT]
        val iconColor = if (theme == THEME_LIGHT) {
            R.color.icon_active
        } else {
            R.color.icon_active_dark
        }

        val drawable = menu!!.getItem(0).icon
        drawable.mutate()
        drawable.setColorFilter(ContextCompat.getColor(context!!, iconColor), PorterDuff.Mode.SRC_IN)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search_store -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    circleReveal(context!!, mView.toolbar_search_store, 1, true, true)
                } else {
                    mView.toolbar_search_store.visibility = View.VISIBLE
                }

                mSearchItem.expandActionView()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
        val icon: Int
        val scrimColor: String
        if (theme == THEME_LIGHT) {
            icon = R.drawable.ic_menu_black
            scrimColor = getString(R.color.scrim)
        } else {
            icon = R.drawable.ic_menu_white
            scrimColor = getString(R.color.scrim_dark)
        }

        (activity as AppCompatActivity).setSupportActionBar(mView.toolbar_store)
        mView.drawer_layout_store.setScrimColor(Color.parseColor(scrimColor))
        mView.toolbar_store.setNavigationIcon(icon)
        mView.toolbar_store.setNavigationOnClickListener {
            mView.drawer_layout_store.openDrawer(Gravity.START)
        }


        mView.nav_view_store.setNavigationItemSelectedListener(this)
        getStoreButtons().forEach { it.setOnClickListener(this) }
        setHasOptionsMenu(true)
    }

    private fun initSearchToolbar() {
        if (mView.toolbar_search_store != null) {
            mView.toolbar_search_store.inflateMenu(R.menu.menu_search_toolbar)
            val toolbarMenu = mView.toolbar_search_store.menu
            mView.toolbar_search_store.setNavigationOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    circleReveal(context!!, mView.toolbar_search_store, 1, true, false)
                } else {
                    mView.toolbar_search_store.visibility = View.GONE
                }
            }

            mSearchItem = toolbarMenu.findItem(R.id.action_filter_search)

            mSearchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(context!!, mView.toolbar_search_store, 1, true, false)
                    } else {
                        mView.toolbar_search_store.visibility = View.GONE
                    }
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    // Do something when expanded
                    return true
                }

            })

            initSearchView(toolbarMenu)
        }
    }

    private fun initSearchView(search_menu: Menu) {
        val searchView = search_menu.findItem(R.id.action_filter_search).actionView as SearchView

        // Enable/Disable Submit button in the keyboard

        searchView.isSubmitButtonEnabled = false

        // Change search close button image

        val closeButton = searchView.findViewById(R.id.search_close_btn) as ImageView
        closeButton.setImageResource(R.drawable.ic_close)


        // set hint and the text colors

        val txtSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        txtSearch.hint = resources.getString(R.string.search)
        txtSearch.setHintTextColor(ContextCompat.getColor(context!!, R.color.text_secondary_dark))
        txtSearch.setTextColor(ContextCompat.getColor(context!!, R.color.text_primary_dark))


        // set the cursor

        val searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            // TODO: if search cursor not visible create own
//            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor) //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
            e.printStackTrace()
        }


        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                //Do searching
                Log.i("query", "" + query)

            }

        })
    }

    private fun initRecyclerViews() {
        mBookAdapter1 = BookAdapter(context!!, true)
        mBookAdapter2 = BookAdapter(context!!, true)
        mBookAdapter3 = BookAdapter(context!!, true)
        mAuthorAdapter = AuthorAdapter(context!!,true)
        mGenreAdapter = GenreAdapter(context!!, true)

        mBookAdapter1.setItemClickListener(object : BookAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
            }
        })

        mBookAdapter2.setItemClickListener(object : BookAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
            }
        })

        mBookAdapter3.setItemClickListener(object : BookAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
            }
        })

        mGenreAdapter.setItemClickListener(object : GenreAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
            }
        })

        mAuthorAdapter.setItemClickListener(object : AuthorAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
            }
        })

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
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        val transaction =  childFragmentManager.beginTransaction()
        transaction.code()
        transaction.commit()
    }

    private fun getRecyclerViews() = arrayOf(mView.rv_pinned_books, mView.rv_most_authors,
            mView.rv_month_books, mView.rv_most_genres, mView.rv_random_books)

    private fun getStoreButtons() = arrayOf(mView.btn_pinned_books, mView.btn_most_authors, mView.btn_most_genres)

}

//private fun initRecyclerView() {
//
//        val linearLayout = LinearLayoutManager(activity)
//        val gridLayout = GridLayoutManager(activity, 3)
//
//        mAdapter = BookAdapter(activity!!)
//        mAdapter.setItemClickListener(object: BookAdapter.OnItemClickListener{
//            override fun onItemClick(id: Int) {
//                val bundle = Bundle()
//                bundle.putInt("id", id)
//                mFragment = BookInfoFragment.newInstance()
//                mFragment!!.arguments = bundle
//
//                changeFragment {
//                    add(R.id.fragment_container, mFragment).addToBackStack(null)
//                }
//            }
//        })
//
//        mView.recycler_view_store.apply {
//            adapter = mAdapter
//            setHasFixedSize(true)
//
//            layoutManager = if (isGridLayout) gridLayout else linearLayout
//
//            addOnScrollListener(object: RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//
//                    totalItemCount = layoutManager.itemCount
//
//                    lastVisibleItem = if (isGridLayout) {
//                        gridLayout.findLastVisibleItemPosition()
//                    } else {
//                        linearLayout.findLastVisibleItemPosition()
//                    }
//
//                    // todo check how is method working (find proper solution)
//                    if (!isLoading &&(lastVisibleItem + visibleThreshold) >= totalItemCount) {
//                        isLoading = true
//                        mNewBooksViewModel.insertAll(++page)
//                    }
//                }
//            })
//        }
//    }