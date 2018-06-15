package com.kitobim.data.remote

import android.content.Context
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.get
import com.kitobim.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    var authApiService: ApiService? = null
    var apiService: ApiService? = null

    fun getAuthService(context: Context) : ApiService {

        val prefs = PreferenceHelper.defaultPrefs(context)
        val token = prefs[Constants.TOKEN, ""]

        if (token == "") return getService()

        if (authApiService == null) {
            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                chain.proceed(newRequest)
            }.build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            authApiService = retrofit.create(ApiService::class.java)
        }
        return authApiService as ApiService
    }

    fun getService() : ApiService {
        if (apiService == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService as ApiService
    }
}
