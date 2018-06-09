package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.FreeBooksDao
import com.kitobim.data.local.database.entity.FreeBooksEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FreeBooksRepository private constructor(application: Application) {
    private val mFreeBooksDao: FreeBooksDao
    private val mService: ApiService
    private val mBookRepo: BookRepository

    companion object {
        private var sInstance: FreeBooksRepository? = null

        fun getInstance(application: Application): FreeBooksRepository {
            if (sInstance == null) {
                sInstance = FreeBooksRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mFreeBooksDao = database.freeBooksDao()
        mService = RetrofitClient.getAuthService(application)
        mBookRepo = BookRepository.getInstance(application)
    }

    fun loadAllBooks() = mFreeBooksDao.loadAllBooks()

    fun insertAll() {
        val result = mFreeBooksDao.getRowCount()

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            rowCount -> if (rowCount < 25) {
                mService.getTopFreeBooks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val list = onSuccess.data
                                    for (item in list) {
                                        mBookRepo.insert(item)
                                        val book = FreeBooksEntity(item.id)
                                        insert(book)
                                    }
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure free books ${onFailure.message}")
                                }
                        )
            }
        }
    }

    fun deleteAll() {
        mFreeBooksDao.deleteAll()
    }

    private fun insert(book: FreeBooksEntity) {
        InsertAsyncTask(mFreeBooksDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: FreeBooksDao)
        : AsyncTask<FreeBooksEntity, Void, Void>() {

        override fun doInBackground(vararg params: FreeBooksEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
