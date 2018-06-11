package com.kitobim.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.WishlistDao
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.WishlistEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WishlistRepository private constructor(application: Application) {
    private val mWishlistDao: WishlistDao
    private val mService: ApiService
    private val mBookRepo: BookRepository

    companion object {
        private var sInstance: WishlistRepository? = null

        fun getInstance(application: Application): WishlistRepository {
            if (sInstance == null) {
                sInstance = WishlistRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mWishlistDao = database.wishlistDao()
        mService = RetrofitClient.getAuthService(application)
        mBookRepo = BookRepository.getInstance(application)
    }

    fun loadAllBooks(): LiveData<List<BookEntity>> {
//        val result = mWishlistDao.getRowCount()

        fetchData()
        return mWishlistDao.loadAllBooks()
    }

    private fun fetchData() {
        mService.getWishlistBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            val books = onSuccess.data
                            mBookRepo.insertAll(books)
                            insertAll(books.map { WishlistEntity(it.id) })
                        },
                        { onFailure ->
                            Log.i("tag", "Failure wishlist ${onFailure.message}")
                        }
                )
    }

    fun deleteAll() {
        mWishlistDao.deleteAll()
    }

    private fun insertAll(books: List<WishlistEntity>) {
        InsertAsyncTask(mWishlistDao).execute(books)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: WishlistDao)
        : AsyncTask<List<WishlistEntity>, Void, Void>() {

        override fun doInBackground(vararg params: List<WishlistEntity>): Void? {
            mAsyncTaskDao.insertAll(params[0])
            return null
        }
    }
}
