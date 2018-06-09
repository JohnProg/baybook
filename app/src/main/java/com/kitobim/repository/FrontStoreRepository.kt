package com.kitobim.repository

import android.app.Application
import android.util.Log
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.ui.custom.FrontStoreListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FrontStoreRepository private constructor(application: Application) {

    private val mService: ApiService = RetrofitClient.getAuthService(application)
    private lateinit var mStoreListener: FrontStoreListener

    companion object {
        private var sInstance: FrontStoreRepository? = null

        fun getInstance(application: Application): FrontStoreRepository {
            if (sInstance == null) {
                sInstance = FrontStoreRepository(application)
            }
            return sInstance!!
        }
    }

    init {
//        val database = AppDatabase.getDatabase(application)
    }
    

    
    fun loadPinnedBooks(){
        mService.getPinnedBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            mStoreListener.setPinnedBooks(onSuccess.data)
                        },
                        { onFailure ->
                            Log.i("tag", "Failure pinned books ${onFailure.message}")
                        }
                )
    }

    fun loadAuthors(){
        mService.getAuthorsFront()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            mStoreListener.setAuthors(onSuccess.data)
                        },
                        { onFailure ->
                            Log.i("tag", "Failure most authors ${onFailure.message}")
                        }
                )

    }

    fun loadTopMonthBooks(){
        mService.getPinnedBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            mStoreListener.setTopMonthBooks(onSuccess.data)
                        },
                        { onFailure ->
                            Log.i("tag", "Failure top month books ${onFailure.message}")
                        }
                )
    }

    fun loadGenres() {
        mService.getGenresFront()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            mStoreListener.setGenres(onSuccess.data)
                        },
                        { onFailure ->
                            Log.i("tag", "Failure most genres${onFailure.message}")
                        }
                )
    }

    fun loadRandomBooks() {
        mService.getRandomBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            mStoreListener.setRandomBooks(onSuccess.data)
                        },
                        { onFailure ->
                            Log.i("tag", "Failure random books${onFailure.message}")
                        }
                )
    }

    fun setStoreListener(listener: FrontStoreListener) {
        mStoreListener = listener
    }
}
