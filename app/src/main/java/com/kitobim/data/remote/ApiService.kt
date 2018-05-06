package com.kitobim.data.remote

import com.kitobim.data.model.BookStore
import com.kitobim.data.model.Login
import com.kitobim.data.model.User
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET(ApiUtils.BOOKS)
    fun getBooks(@Query("page") page: Int, @Query("q") search: String?): Flowable<BookStore>

    @POST(ApiUtils.SIGN_IN)
    fun login(@Body login: Login): Call<User>

}