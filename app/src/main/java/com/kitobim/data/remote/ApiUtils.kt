package com.kitobim.data.remote

object ApiUtils {

//    const val BASE_URL = "http://kitobim.com/api/0.0.9/"
    const val BASE_URL = "http://development.baysoftware.ru/api/"


//    const val BOOKS = "books/"
//    const val SIGN_IN = "signin/"

    const val REGISTER = "register"
    const val LOGIN = "login"
    const val CONFIRM_PHONE = "register/confirm"

    const val AUTHORS = "authors"
    const val AUTHOR_INFO = "authors/{id}"
    const val AUTHORS_MOST = "authors/filter/most"
    const val AUTHOR_BOOKS = "authors/{id}/books"

    const val GENRES = "genres"
    const val GENRES_MOST = "genres/filter/most"
    const val GENRE_BOOKS = "genres/{id}/books"

    const val PUBLISHERS = "publishers"
    const val PUBLISHER_BOOKS = "publishers/{id}/books"

    const val COLLECTIONS = "collections"
    const val COLLECTION_BOOKS = "collections/{id}/books"

    const val BOOK_INFO = "books/{id}"
    const val BOOK_FILE = "books/{id}/download"

    const val WISHLIST = "wishlist"
    const val NEW_BOOKS = "books/filter/new"
    const val RECOMMENDED_BOOKS = "books/filter/recommended"
    const val PINNED_BOOKS = "books/filter/pinned"
    const val RANDOM_BOOKS = "books/filter/random"
    const val TOP_MONTH_BOOKS = "books/top/month"
    const val TOP_FREE_BOOKS = "books/top/free"
    const val TOP_PAID_BOOKS = "books/top/paid"
    const val TOP_RATED_BOOKS=  "books/top/rated"

}
