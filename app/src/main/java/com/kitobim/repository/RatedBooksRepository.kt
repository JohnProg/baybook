package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.RatedBooksDao
import com.kitobim.data.local.database.entity.RatedBooksEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RatedBooksRepository private constructor(application: Application) {
    private val mRatedBooksDao: RatedBooksDao
    private val mService: ApiService
    private val mBookRepo: BookRepository

    companion object {
        private var sInstance: RatedBooksRepository? = null

        fun getInstance(application: Application): RatedBooksRepository {
            if (sInstance == null) {
                sInstance = RatedBooksRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mRatedBooksDao = database.ratedBooksDao()
        mService = RetrofitClient.getAuthService(application)
        mBookRepo = BookRepository.getInstance(application)
    }

    fun loadAllBooks() = mRatedBooksDao.loadAllBooks()

    fun insertAll() {
        val result = mRatedBooksDao.getRowCount()

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            rowCount -> if (rowCount < 25) {
                mService.getTopRatedBooks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val list = onSuccess.data
                                    for (item in list) {
                                        mBookRepo.insert(item)
                                        val book = RatedBooksEntity(item.id)
                                        insert(book)
                                    }
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure rated books ${onFailure.message}")
                                }
                        )
            }
        }
    }

    fun deleteAll() {
        mRatedBooksDao.deleteAll()
    }

    private fun insert(book: RatedBooksEntity) {
        InsertAsyncTask(mRatedBooksDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: RatedBooksDao)
        : AsyncTask<RatedBooksEntity, Void, Void>() {

        override fun doInBackground(vararg params: RatedBooksEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
