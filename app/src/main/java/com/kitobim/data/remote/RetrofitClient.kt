package com.kitobim.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    fun getService(token: String? = null): ApiService {

        val retrofit: Retrofit

        if (token != "") {
            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Token $token")
                        .build()
                chain.proceed(newRequest)
            }.build()

            retrofit = Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        } else {
            retrofit = Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit.create(ApiService::class.java)
    }
}
