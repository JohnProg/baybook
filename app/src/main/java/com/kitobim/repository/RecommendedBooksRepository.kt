package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.RecommendedBooksDao
import com.kitobim.data.local.database.entity.RecommendedBooksEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecommendedBooksRepository private constructor(application: Application) {
    private val mRecommendedBooksDao: RecommendedBooksDao
    private val mService: ApiService
    private val mBookRepo: BookRepository

    companion object {
        private var sInstance: RecommendedBooksRepository? = null

        fun getInstance(application: Application): RecommendedBooksRepository {
            if (sInstance == null) {
                sInstance = RecommendedBooksRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mRecommendedBooksDao = database.recommendedBooksDao()
        mService = RetrofitClient.getAuthService(application)
        mBookRepo = BookRepository.getInstance(application)
    }

    fun loadAllBooks() = mRecommendedBooksDao.loadAllBooks()

    fun insertAll() {
        val result = mRecommendedBooksDao.getRowCount()
        Log.i("tag", "Recommended books")
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            rowCount -> if (rowCount < 25) {
            Log.i("tag", "row count $rowCount")
                mService.getRecommendedBooks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { onSuccess ->
                                    val books = onSuccess.data
                                    for (book in books) {
                                        mBookRepo.insert(book)
                                        val item = RecommendedBooksEntity(book.id)
                                        insert(item)
                                    }
                                },
                                { onFailure ->
                                    Log.i("tag", "Failure recommended books ${onFailure.message}")
                                }
                        )
            }
        }
    }

    fun deleteAll() {
        mRecommendedBooksDao.deleteAll()
    }

    private fun insert(book: RecommendedBooksEntity) {
        InsertAsyncTask(mRecommendedBooksDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: RecommendedBooksDao)
        : AsyncTask<RecommendedBooksEntity, Void, Void>() {

        override fun doInBackground(vararg params: RecommendedBooksEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
