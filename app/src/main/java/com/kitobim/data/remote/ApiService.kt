package com.kitobim.data.remote

import com.kitobim.data.model.*
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

//    @GET(ApiUtils.BOOKS)
//    fun getBooks(@Query("page") page: Int, @Query("q") search: String?): Flowable<BookStore>

//    @POST(ApiUtils.SIGN_IN)
//    fun login(@Body login: Login): Call<User>

    @POST(ApiUtils.REGISTER) fun register(@Body register: Register): Call<User>

    @POST(ApiUtils.LOGIN) fun login(@Body login: Login): Call<User>

    @GET(ApiUtils.BOOKS) fun getBooks(@Query("page") page: Int): Flowable<Library>

    @GET(ApiUtils.BOOK) fun getBook(@Path("id") id: Int): Flowable<Book>


}