package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.AuthorDao
import com.kitobim.data.local.database.entity.AuthorEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthorRepository private constructor(application: Application) {
    private val mAuthorDao: AuthorDao
    private val mService: ApiService

    companion object {
        private var sInstance: AuthorRepository? = null

        fun getInstance(application: Application): AuthorRepository {
            if (sInstance == null) {
                sInstance = AuthorRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mAuthorDao = database.authorDao()
        mService = RetrofitClient.getAuthService(application)
    }

    fun loadAllBooks() = mAuthorDao.loadAllAuthors()

    fun insertAll(page: Int) {
        val existedPage = mAuthorDao.getNumberOfThisPage(page) > 0

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
                                }
                            },
                            {
                                onFailure -> Log.i("tag", "Failure ${onFailure.message}")
                            }
                    )
    }

    fun deleteAll() {
        mAuthorDao.deleteAll()
    }

    private fun insert(book: AuthorEntity) {
        InsertAsyncTask(mAuthorDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: AuthorDao)
        : AsyncTask<AuthorEntity, Void, Void>() {

        override fun doInBackground(vararg params: AuthorEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
