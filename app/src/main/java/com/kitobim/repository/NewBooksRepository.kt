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
    private val mNewBooksDao: NewBooksDao
    private val mService: ApiService
    private val mBookRepo: BookRepository
    private var mLastPage = 1

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
        mNewBooksDao = database.newBooksDao()
        mService = RetrofitClient.getAuthService(application)
        mBookRepo = BookRepository.getInstance(application)
    }

    fun loadAllBooks() = mNewBooksDao.loadAllBooks()

    fun insertAll(page: Int) {
        val result = mNewBooksDao.getRowCountOfPage(page)

        if (mLastPage >= page) {
            result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { rowCount ->
                if (rowCount == 0) {
                    mService.getNewBooks(page)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onSuccess ->
                                        val list = onSuccess.data
                                        mLastPage = onSuccess.meta.last_page
                                        for (item in list) {
                                            mBookRepo.insert(item)
                                            val book = NewBooksEntity(item.id, page)
                                            insert(book)
                                        }
                                    },
                                    { onFailure ->
                                        Log.i("tag", "Failure new books ${onFailure.message}")
                                    }
                            )

                }
            }
        }
    }

    fun deleteAll() {
        mNewBooksDao.deleteAll()
    }

    private fun insert(book: NewBooksEntity) {
        InsertAsyncTask(mNewBooksDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: NewBooksDao)
        : AsyncTask<NewBooksEntity, Void, Void>() {

        override fun doInBackground(vararg params: NewBooksEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
