package com.kitobim.repository

import android.app.Application
import android.arch.lifecycle.LiveData
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
            return sInstance as AuthorRepository
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mAuthorDao = database.authorDao()
        mService = RetrofitClient.getAuthService(application)
    }

    fun deleteAll() {
        mAuthorDao.deleteAll()
    }
    fun getAuthorById(id: Int): LiveData<AuthorEntity> {
        Log.i("tag", "fetch author id: $id")
        fetchData(id)
        return mAuthorDao.getAuthorById(id)
    }

    private fun fetchData(id: Int) {
        mService.getAuthor(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            val author = onSuccess.data
                            update(author)
                        },
                        { onFailure ->
                            Log.i("tag", "Failure author info ${onFailure.message}")
                        }
                )
    }

    fun insert(author: AuthorEntity) {
        InsertAsyncTask(mAuthorDao).execute(author)
    }

    fun insertAll(authors: List<AuthorEntity>) {
        InsertAllAsyncTask(mAuthorDao).execute(authors)
    }

    fun update(author: AuthorEntity) {
        UpdateAsyncTask(mAuthorDao).execute(author)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: AuthorDao)
        : AsyncTask<AuthorEntity, Void, Void>() {

        override fun doInBackground(vararg params: AuthorEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class InsertAllAsyncTask internal constructor(private val mAsyncTaskDao: AuthorDao)
        : AsyncTask<List<AuthorEntity>, Void, Void>() {

        override fun doInBackground(vararg params: List<AuthorEntity>): Void? {
            mAsyncTaskDao.insertAll(params[0])
            return null
        }
    }

    private class UpdateAsyncTask internal constructor(private val mAsyncTaskDao: AuthorDao)
        : AsyncTask<AuthorEntity, Void, Void>() {

        override fun doInBackground(vararg params: AuthorEntity): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }
}
