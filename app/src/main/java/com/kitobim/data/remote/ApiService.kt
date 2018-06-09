package com.kitobim.data.remote

import com.kitobim.data.model.*
import com.kitobim.data.remote.ApiUtils.AUTHORS
import com.kitobim.data.remote.ApiUtils.AUTHORS_MOST
import com.kitobim.data.remote.ApiUtils.AUTHOR_BOOKS
import com.kitobim.data.remote.ApiUtils.AUTHOR_INFO
import com.kitobim.data.remote.ApiUtils.BOOK_INFO
import com.kitobim.data.remote.ApiUtils.COLLECTIONS
import com.kitobim.data.remote.ApiUtils.COLLECTION_BOOKS
import com.kitobim.data.remote.ApiUtils.GENRES
import com.kitobim.data.remote.ApiUtils.GENRES_MOST
import com.kitobim.data.remote.ApiUtils.GENRE_BOOKS
import com.kitobim.data.remote.ApiUtils.LOGIN
import com.kitobim.data.remote.ApiUtils.NEW_BOOKS
import com.kitobim.data.remote.ApiUtils.PINNED_BOOKS
import com.kitobim.data.remote.ApiUtils.PUBLISHERS
import com.kitobim.data.remote.ApiUtils.PUBLISHER_BOOKS
import com.kitobim.data.remote.ApiUtils.RANDOM_BOOKS
import com.kitobim.data.remote.ApiUtils.RECOMMENDED_BOOKS
import com.kitobim.data.remote.ApiUtils.REGISTER
import com.kitobim.data.remote.ApiUtils.TOP_FREE_BOOKS
import com.kitobim.data.remote.ApiUtils.TOP_MONTH_BOOKS
import com.kitobim.data.remote.ApiUtils.TOP_PAID_BOOKS
import com.kitobim.data.remote.ApiUtils.TOP_RATED_BOOKS
import com.kitobim.data.remote.ApiUtils.WISHLIST
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

//    @GET(ApiUtils.BOOKS)
//    fun getBooks(@Query("page") page: Int, @Query("q") search: String?): Flowable<BookStore>

//    @POST(ApiUtils.SIGN_IN)
//    fun login(@Body login: Login): Call<User>

    @POST(REGISTER) fun register(@Body register: Register): Call<User>

    @POST(LOGIN) fun login(@Body login: Login): Call<User>

    @GET(BOOK_INFO) fun getBook(@Path("id") id: Int): Flowable<Books>

    @GET(WISHLIST) fun getWishlistBooks(): Flowable<Books>
    @GET(RECOMMENDED_BOOKS) fun getRecommendedBooks(): Flowable<Books>
    @GET(PINNED_BOOKS) fun getPinnedBooks(): Flowable<Books>
    @GET(NEW_BOOKS) fun getNewBooks(@Query("page") page: Int): Flowable<Books>
    @GET(TOP_FREE_BOOKS) fun getTopFreeBooks(): Flowable<Books>
    @GET(TOP_PAID_BOOKS) fun getTopPaidBooks(): Flowable<Books>
    @GET(TOP_RATED_BOOKS) fun getTopRatedBooks(): Flowable<Books>
    @GET(TOP_MONTH_BOOKS) fun getTopMonthBooks(): Flowable<Books>
    @GET(RANDOM_BOOKS) fun getRandomBooks(): Flowable<Books>

    @GET(GENRES) fun getAllGenres(@Query("page") page: Int): Flowable<Genres>
    @GET(GENRES_MOST) fun getGenresFront(): Flowable<Genres>
    @GET(GENRE_BOOKS) fun getGenreBooks(@Path("id") id: Int): Flowable<Books>

    @GET(PUBLISHERS) fun getAllPublishers(): Flowable<Publishers>
    @GET(PUBLISHER_BOOKS) fun getPublisherBooks(@Path("id") id: Int): Flowable<Books>

    @GET(AUTHORS) fun getAllAuthors(@Query("page") page: Int): Flowable<Authors>
    @GET(AUTHORS_MOST) fun getAuthorsFront(): Flowable<Authors>
    @GET(AUTHOR_INFO) fun getAuthor(@Path("id") id: Int): Flowable<Authors>
    @GET(AUTHOR_BOOKS) fun getAuthorBooks(@Path("id") id: Int): Flowable<Books>

    @GET(COLLECTIONS) fun getAllCollections(): Flowable<Collections>
    @GET(COLLECTION_BOOKS) fun getCollectionBooks(@Path("id") id: Int): Flowable<Books>




}