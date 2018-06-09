package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.PublisherDao
import com.kitobim.data.local.database.entity.PublisherEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PublisherRepository private constructor(application: Application) {
    private val mPublisherDao: PublisherDao
    private val mService: ApiService

    companion object {
        private var sInstance: PublisherRepository? = null

        fun getInstance(application: Application): PublisherRepository {
            if (sInstance == null) {
                sInstance = PublisherRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mPublisherDao = database.publisherDao()
        mService = RetrofitClient.getAuthService(application)
    }

    fun loadAllPublishers() = mPublisherDao.loadAllPublishers()

    fun insertAll() {
        val result = mPublisherDao.getRowCount()

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            rowCount -> if (rowCount == 0) {
                mService.getAllPublishers()
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
                                    Log.i("tag", "Failure publishers ${onFailure.message}")
                                }
                        )
            }
        }
    }

    fun deleteAll() {
        mPublisherDao.deleteAll()
    }

    private fun insert(publisher: PublisherEntity) {
        InsertAsyncTask(mPublisherDao).execute(publisher)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: PublisherDao)
        : AsyncTask<PublisherEntity, Void, Void>() {

        override fun doInBackground(vararg params: PublisherEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
