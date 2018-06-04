package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.LibraryDao
import com.kitobim.data.local.database.entity.LibraryEntity


class LibraryRepository private constructor(application: Application) {

    private var mLibraryDao: LibraryDao

    companion object {
        private var sInstance: LibraryRepository? = null

        fun getInstance(application: Application): LibraryRepository {
            if (sInstance == null) {
                sInstance = LibraryRepository(application)
            }
            return sInstance!!
        }
    }

    init{
        val database = AppDatabase.getDatabase(application)
        mLibraryDao = database.libraryDao()
    }



    fun loadBooksByNameAsc() = mLibraryDao.loadBooksByNameAsc()

    fun loadAllBooks() = mLibraryDao.loadAllBooks()


    fun insert(book: LibraryEntity) {
        InsertAsyncTask(mLibraryDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: LibraryDao)
        : AsyncTask<LibraryEntity, Void, Void>() {

        override fun doInBackground(vararg params: LibraryEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}