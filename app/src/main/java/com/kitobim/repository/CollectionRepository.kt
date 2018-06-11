package com.kitobim.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.CollectionDao
import com.kitobim.data.local.database.entity.CollectionEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CollectionRepository private constructor(application: Application) {
    private val mCollectionDao: CollectionDao
    private val mService: ApiService

    companion object {
        private var sInstance: CollectionRepository? = null

        fun getInstance(application: Application): CollectionRepository {
            if (sInstance == null) {
                sInstance = CollectionRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mCollectionDao = database.collectionDao()
        mService = RetrofitClient.getAuthService(application)
    }

    fun loadAllCollections():LiveData<List<CollectionEntity>>  {
        val result = mCollectionDao.getRowCount()

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { rowCount ->
            if (rowCount == 0) {
                fetchData()
            }
        }
        return mCollectionDao.loadAllCollections()
    }

    private fun fetchData() {
        mService.getAllCollections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            val list = onSuccess.data
                            for (item in list) {
                                insert(item)
                            }
                        },
                        { onFailure ->
                            Log.i("tag", "Failure collections ${onFailure.message}")
                        }
                )
    }

    fun deleteAll() {
        mCollectionDao.deleteAll()
    }

    private fun insert(collection: CollectionEntity) {
        InsertAsyncTask(mCollectionDao).execute(collection)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: CollectionDao)
        : AsyncTask<CollectionEntity, Void, Void>() {

        override fun doInBackground(vararg params: CollectionEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
