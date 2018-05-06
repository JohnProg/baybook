package com.kitobim.data.remote

import android.content.Context
import android.util.Log
import com.kitobim.Constants
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.get
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response? {

        val prefs = PreferenceHelper.defaultPrefs(context)
        val token = prefs[Constants.TOKEN, ""]

        if (chain != null) {
            val authenticatedRequest = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Token $token")
                    .build()

            val response = chain.proceed(authenticatedRequest)

            if (response.code() == 404) {
                Log.e("error", "Interceptor error 404")
            }
            return response
        }
        return null
    }
}
