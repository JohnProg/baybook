package com.kitobim.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.GenreListDao
import com.kitobim.data.local.database.entity.GenreEntity
import com.kitobim.data.local.database.entity.GenreListEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GenreListRepository private constructor(application: Application) {
    private val mGenreListDao: GenreListDao
    private val mService: ApiService
    private val mGenreRepo: GenreRepository

    companion object {
        private var sInstance: GenreListRepository? = null

        fun getInstance(application: Application): GenreListRepository {
            if (sInstance == null) {
                sInstance = GenreListRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mGenreListDao = database.genreListDao()
        mService = RetrofitClient.getAuthService(application)
        mGenreRepo = GenreRepository.getInstance(application)
    }

    fun loadAllGenres() = mGenreListDao.loadAllGenres()

    fun loadGenresByPage(page: Int): LiveData<List<GenreEntity>> {
        val result = mGenreListDao.getRowCountOfPage(page)

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { rowCount ->
            if (rowCount < Constants.GENRE_PAGE_THRESHOLD) {
                fetchData(page)
            }
        }
        return mGenreListDao.loadAllGenres()
    }

    private fun fetchData(page: Int) {
        mService.getAllGenres(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            val list = onSuccess.data
                            for (item in list) {
                                mGenreRepo.insert(item)
                                val genre = GenreListEntity(item.id, page)
                                insert(genre)
                            }
                        },
                        { onFailure ->
                            Log.i("tag", "Failure genre list ${onFailure.message}")
                        }
                )
    }

    fun deleteAll() {
        mGenreListDao.deleteAll()
    }

    private fun insert(Genre: GenreListEntity) {
        InsertAsyncTask(mGenreListDao).execute(Genre)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: GenreListDao)
        : AsyncTask<GenreListEntity, Void, Void>() {

        override fun doInBackground(vararg params: GenreListEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
