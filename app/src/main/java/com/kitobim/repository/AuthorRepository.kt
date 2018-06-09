package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.AuthorDao
import com.kitobim.data.local.database.entity.AuthorEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient

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

    fun insert(author: AuthorEntity) {
        InsertAsyncTask(mAuthorDao).execute(author)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: AuthorDao)
        : AsyncTask<AuthorEntity, Void, Void>() {

        override fun doInBackground(vararg params: AuthorEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
