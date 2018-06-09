package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.BookDao
import com.kitobim.data.local.database.entity.BookEntity


class BookRepository private constructor(application: Application) {

    private var mBookDao: BookDao

    companion object {
        private var sInstance: BookRepository? = null

        fun getInstance(application: Application): BookRepository {
            if (sInstance == null) {
                sInstance = BookRepository(application)
            }
            return sInstance!!
        }
    }

    init{
        val database = AppDatabase.getDatabase(application)
        mBookDao = database.bookDao()
    }

    fun insert(book: BookEntity) {
        InsertAsyncTask(mBookDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: BookDao)
        : AsyncTask<BookEntity, Void, Void>() {

        override fun doInBackground(vararg params: BookEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}