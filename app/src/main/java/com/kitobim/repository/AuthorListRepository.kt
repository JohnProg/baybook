package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.AuthorListDao
import com.kitobim.data.local.database.entity.AuthorListEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AuthorListRepository private constructor(application: Application) {
    private val mAuthorListDao: AuthorListDao
    private val mService: ApiService
    private val mAuthorRepo: AuthorRepository
    private var mLastPage = 1

    companion object {
        private var sInstance: AuthorListRepository? = null

        fun getInstance(application: Application): AuthorListRepository {
            if (sInstance == null) {
                sInstance = AuthorListRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mAuthorListDao = database.authorListDao()
        mService = RetrofitClient.getAuthService(application)
        mAuthorRepo = AuthorRepository.getInstance(application)
    }

    fun loadAllAuthors() = mAuthorListDao.loadAllAuthors()

    fun insertAll(page: Int) {
        val result = mAuthorListDao.getRowCountOfPage(page)

        if (mLastPage >= page) {
            result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                rowCount -> if (rowCount == 0) {
                    mService.getAllAuthors(page)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { onSuccess ->
                                        val list = onSuccess.data
                                        mLastPage = onSuccess.meta.last_page
                                        for (item in list) {
                                            mAuthorRepo.insert(item)
                                            val author = AuthorListEntity(item.id, page)
                                            insert(author)
                                        }
                                    },
                                    { onFailure ->
                                        Log.i("tag", "Failure author list ${onFailure.message}")
                                    }
                            )
                }

            }
        }
    }

    fun deleteAll() {
        mAuthorListDao.deleteAll()
    }

    private fun insert(Author: AuthorListEntity) {
        InsertAsyncTask(mAuthorListDao).execute(Author)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: AuthorListDao)
        : AsyncTask<AuthorListEntity, Void, Void>() {

        override fun doInBackground(vararg params: AuthorListEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}
