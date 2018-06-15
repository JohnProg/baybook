package com.kitobim.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.BookDao
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream


class BookRepository private constructor(private val application: Application) {

    private val mBookDao: BookDao
    private val mService: ApiService

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
        mService = RetrofitClient.getAuthService(application)
    }

    fun getBookById(id: Int):LiveData<BookEntity> {
        fetchData(id)
        return mBookDao.getBookById(id)
    }

    private fun fetchData(id: Int) {
        mService.getBook(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            val book = onSuccess.data
                            update(book)
                        },
                        { onFailure ->
                            Log.i("tag", "Failure book info ${onFailure.message}")
                        }
                )
    }

    fun downloadBook(id: Int) {
        mService.getBookFile(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response ->
                    val bis = BufferedInputStream(response.byteStream())
                    val outputFile = File(application.filesDir, "$id.epub")
                    val fos = FileOutputStream(outputFile)
                    val buff = ByteArray(5 * 1024)
                    var len: Int
                    len = bis.read(buff)
                    while (len != -1) {
                        fos.write(buff, 0, len)
                        len =  bis.read(buff)
                    }

                    fos.flush()
                    fos.close()
                    bis.close()
                },
                {
                    onFailure -> Log.i("tag", "Failure download file ${onFailure.message}")
                })
    }

    fun insert(book: BookEntity) {
         InsertAsyncTask(mBookDao).execute(book)
    }

    fun insertAll(books: List<BookEntity>) {
        InsertAllAsyncTask(mBookDao).execute(books)
    }

    fun update(book: BookEntity) {
        UpdateAsyncTask(mBookDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: BookDao)
        : AsyncTask<BookEntity, Void, Void>() {

        override fun doInBackground(vararg params: BookEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class InsertAllAsyncTask internal constructor(private val mAsyncTaskDao: BookDao)
        : AsyncTask<List<BookEntity>, Void, Void>() {

        override fun doInBackground(vararg params: List<BookEntity>): Void? {
            mAsyncTaskDao.insertAll(params[0])
            return null
        }
    }

    private class UpdateAsyncTask internal constructor(private val mAsyncTaskDao: BookDao)
        : AsyncTask<BookEntity, Void, Void>() {

        override fun doInBackground(vararg params: BookEntity): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }
}