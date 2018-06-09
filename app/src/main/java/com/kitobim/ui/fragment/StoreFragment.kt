package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
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
import com.kitobim.util.Constants.THEME
import com.kitobim.util.Constants.THEME_LIGHT
import kotlinx.android.synthetic.main.fragment_store.view.*






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
    private var mCurrentNavId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_store, container, false)

        initToolbar()
        initRecyclerViews()
        updateUi()

        return mView
    }

    override fun onClick(v: View?) {

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (mCurrentNavId == item.itemId) {
            mView.drawer_layout_store.closeDrawer(Gravity.START)
            return false
        }

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
        mView.drawer_layout_store.closeDrawer(Gravity.START)
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
                    mBookAdapter1.updateBooks(list)
                }

                override fun setTopMonthBooks(list: List<BookEntity>) {
                    mBookAdapter2.updateBooks(list)
                }

                override fun setRandomBooks(list: List<BookEntity>) {
                    mBookAdapter3.updateBooks(list)
                }

                override fun setAuthors(list: List<AuthorEntity>) {
                    mAuthorAdapter.updateAuthors(list)
                }

                override fun setGenres(list: List<GenreEntity>) {
                    mGenreAdapter.updateGenres(list)
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
        getButtons().forEach { it.setOnClickListener(this) }
        setHasOptionsMenu(true)
    }

    private fun initRecyclerViews() {
        mBookAdapter1 = BookAdapter(context!!, true)
        mBookAdapter2 = BookAdapter(context!!, true)
        mBookAdapter3 = BookAdapter(context!!, true)
        mAuthorAdapter = AuthorAdapter(context!!,true)
        mGenreAdapter = GenreAdapter(context!!)

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
                R.id.rv_front1 -> it.adapter = mBookAdapter1
                R.id.rv_front2 -> it.adapter = mAuthorAdapter
                R.id.rv_front3 -> it.adapter = mBookAdapter2
                R.id.rv_front4 -> it.adapter = mGenreAdapter
                R.id.rv_front5 -> it.adapter = mBookAdapter3
            }
        }
    }

    private fun getRecyclerViews() = arrayOf(mView.rv_front1, mView.rv_front2,
            mView.rv_front3, mView.rv_front4, mView.rv_front5)

    private fun getButtons() = arrayOf(mView.btn_front1, mView.btn_front2, mView.btn_front4)

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        val transaction =  childFragmentManager.beginTransaction()
        transaction.code()
        transaction.commit()
    }
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