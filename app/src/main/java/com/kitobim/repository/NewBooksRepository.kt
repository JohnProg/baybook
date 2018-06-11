package com.kitobim.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import com.kitobim.data.local.database.AppDatabase
import com.kitobim.data.local.database.dao.NewBooksDao
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.NewBooksEntity
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewBooksRepository private constructor(application: Application) {
    private val mNewBooksDao: NewBooksDao
    private val mService: ApiService
    private val mBookRepo: BookRepository

    companion object {
        private var sInstance: NewBooksRepository? = null

        fun getInstance(application: Application): NewBooksRepository {
            if (sInstance == null) {
                sInstance = NewBooksRepository(application)
            }
            return sInstance!!
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        mNewBooksDao = database.newBooksDao()
        mService = RetrofitClient.getAuthService(application)
        mBookRepo = BookRepository.getInstance(application)
    }

    fun loadAllNewBooks() = mNewBooksDao.loadAllBooks()

    fun loadBookByPage(page: Int) {
        val result = mNewBooksDao.getRowCountOfPage(page)

        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { rowCount ->
            if (rowCount < Constants.BOOK_PAGE_THRESHOLD) {
                fetchData(page)
            }
        }
    }

    private fun fetchData(page: Int): List<BookEntity> {
        var books: List<BookEntity> = emptyList()
        mService.getNewBooks(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            books = onSuccess.data
                            mBookRepo.insertAll(books)
                            insertAll(books.map { NewBooksEntity(it.id, page)})
                        },
                        { onFailure ->
                            Log.i("tag", "Failure new books ${onFailure.message}")
                            books = emptyList()
                        }
                )
        return books
    }

    fun deleteAll() {
        mNewBooksDao.deleteAll()
    }

    private fun insertAll(books: List<NewBooksEntity>) {
        InsertAsyncTask(mNewBooksDao).execute(books)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: NewBooksDao)
        : AsyncTask<List<NewBooksEntity>, Void, Void>() {

        override fun doInBackground(vararg params: List<NewBooksEntity>): Void? {
            mAsyncTaskDao.insertAll(params[0])
            return null
        }
    }
}
