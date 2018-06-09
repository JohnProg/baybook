package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.GenreDao
import com.kitobim.data.local.database.entity.GenreEntity


class GenreRepository private constructor(application: Application) {

    private var mGenreDao: GenreDao

    companion object {
        private var sInstance: GenreRepository? = null

        fun getInstance(application: Application): GenreRepository {
            if (sInstance == null) {
                sInstance = GenreRepository(application)
            }
            return sInstance!!
        }
    }

    init{
        val database = AppDatabase.getDatabase(application)
        mGenreDao = database.genreDao()
    }


    fun insert(genre: GenreEntity) {
        InsertAsyncTask(mGenreDao).execute(genre)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: GenreDao)
        : AsyncTask<GenreEntity, Void, Void>() {

        override fun doInBackground(vararg params: GenreEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}