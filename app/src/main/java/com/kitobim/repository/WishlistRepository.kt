package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.WishlistDao
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

    fun loadAllBooks() = mWishlistDao.loadAllBooks()

    fun insertAll() {
        val result = mWishlistDao.getRowCount()

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            rowCount -> if (rowCount == 0) {
            mService.getWishlistBooks()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { onSuccess ->
                                val list = onSuccess.data
                                for (item in list) {
                                    mBookRepo.insert(item)
                                    val book = WishlistEntity(item.id)
                                    insert(book)
                                }
                            },
                            { onFailure ->
                                Log.i("tag", "Failure wishlist ${onFailure.message}")
                            }
                    )
            }
        }
    }

    fun deleteAll() {
        mWishlistDao.deleteAll()
    }

    private fun insert(book: WishlistEntity) {
        InsertAsyncTask(mWishlistDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: WishlistDao)
        : AsyncTask<WishlistEntity, Void, Void>() {

        override fun doInBackground(vararg params: WishlistEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
