package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.PaidBooksDao
import com.kitobim.data.local.database.entity.PaidBooksEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PaidBooksRepository private constructor(application: Application) {
    private val mPaidBooksDao: PaidBooksDao
    private val mService: ApiService
    private val mBookRepo: BookRepository

    companion object {
        private var sInstance: PaidBooksRepository? = null

        fun getInstance(application: Application): PaidBooksRepository {
            if (sInstance == null) {
                sInstance = PaidBooksRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mPaidBooksDao = database.paidBooksDao()
        mService = RetrofitClient.getAuthService(application)
        mBookRepo = BookRepository.getInstance(application)
    }

    fun loadAllBooks() = mPaidBooksDao.loadAllBooks()

    fun insertAll() {
        val result = mPaidBooksDao.getRowCount()

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            rowCount -> if (rowCount < 25) {
                mService.getTopPaidBooks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val list = onSuccess.data
                                    for (item in list) {
                                        mBookRepo.insert(item)
                                        val book = PaidBooksEntity(item.id)
                                        insert(book)
                                    }
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure paid books ${onFailure.message}")
                                }
                        )
            }
        }
    }

    fun deleteAll() {
        mPaidBooksDao.deleteAll()
    }

    private fun insert(book: PaidBooksEntity) {
        InsertAsyncTask(mPaidBooksDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: PaidBooksDao)
        : AsyncTask<PaidBooksEntity, Void, Void>() {

        override fun doInBackground(vararg params: PaidBooksEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
