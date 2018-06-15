package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.*

class StoreViewModel(application: Application) : AndroidViewModel(application) {


    private val authorsRepo = AuthorListRepository.getInstance(application)
    private val genresRepo = GenreListRepository.getInstance(application)
    private val publishersRepo = PublisherRepository.getInstance(application)
    private val collectionsRepo = CollectionRepository.getInstance(application)
    private val wishlistRepo = WishlistRepository.getInstance(application)
    private val recommendedBooksRepo = RecommendedBooksRepository.getInstance(application)
    private val newBooksRepo = NewBooksRepository.getInstance(application)
    private val paidBooksRepo = PaidBooksRepository.getInstance(application)
    private val freeBooksRepo = FreeBooksRepository.getInstance(application)
    private val ratedBooksRepo = RatedBooksRepository.getInstance(application)


    fun loadAllAuthors() = authorsRepo.loadAllAuthors()
    fun loadAuthorsByPage(page: Int) = authorsRepo.loadAuthorsByPage(page)
    fun loadAllGenres() = genresRepo.loadAllGenres()
    fun loadGenresByPage(page: Int) = genresRepo.loadGenresByPage(page)
    fun loadAllPublishers() = publishersRepo.loadAllPublishers()
    fun loadAllCollections() = collectionsRepo.loadAllCollections()
    fun loadAllWishlist() = wishlistRepo.loadAllBooks()
    fun loadRecommendedBooks() = recommendedBooksRepo.loadAllBooks()
    fun loadAllNewBooks() = newBooksRepo.loadAllNewBooks()
    fun loadNewBooksByPage(page: Int) = newBooksRepo.loadBookByPage(page)
    fun loadAllFreeBooks() = freeBooksRepo.loadAllBooks()
    fun loadAllPaidBooks() = paidBooksRepo.loadAllBooks()
    fun loadAllRatedBooks() = ratedBooksRepo.loadAllBooks()

}