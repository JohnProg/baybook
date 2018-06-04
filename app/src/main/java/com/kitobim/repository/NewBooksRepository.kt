package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.NewBooksDao
import com.kitobim.data.local.database.entity.NewBooksEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewBooksRepository private constructor(application: Application) {
    private val mBookDao: NewBooksDao
    private val mService: ApiService

    companion object {
        private var sInstance: NewBooksRepository? = null

        fun getInstance(application: Application): NewBooksRepository {
            if (sInstance == null) {
                sInstance = NewBooksRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mBookDao = database.newBookDao()
        mService = RetrofitClient.getAuthService(application)
    }

    fun loadAllBooks() = mBookDao.loadAllBooks()

    fun insertAll(page: Int) {
        val existedPage = mBookDao.getNumberOfThisPage(page) > 0

        if (!existedPage)
        mService.getBooks(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onSuccess ->
                            Log.i("tag", "onSuccess")
                            val list = onSuccess.data
                            for (item in list) {
                                val book = NewBooksEntity(0, item.id, page)
                                insert(book)
                            }
                        },
                        {
                            onFailure -> Log.i("tag", "Failure ${onFailure.message}")
                        }
                )
    }

    fun deleteAll() {
        mBookDao.deleteAll()
    }

    private fun insert(book: NewBooksEntity) {
        InsertAsyncTask(mBookDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: NewBooksDao)
        : AsyncTask<NewBooksEntity, Void, Void>() {

        override fun doInBackground(vararg params: NewBooksEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
