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

    fun getAllAuthors() = authorsRepo.loadAllAuthors()
    fun getAllGenres() = genresRepo.loadAllGenres()
    fun getAllPublishers() = publishersRepo.loadAllPublishers()
    fun getAllCollections() = collectionsRepo.loadAllCollections()
    fun getAllWishlist() = wishlistRepo.loadAllBooks()
    fun getAllRecommendedBooks() = recommendedBooksRepo.loadAllBooks()
    fun getAllNewBooks() = newBooksRepo.loadAllBooks()
    fun getAllFreeBooks() = freeBooksRepo.loadAllBooks()
    fun getAllPaidBooks() = paidBooksRepo.loadAllBooks()
    fun getAllRatedBooks() = ratedBooksRepo.loadAllBooks()

    fun insertAllAuthors(page: Int) { authorsRepo.insertAll(page) }
    fun insertAllGenres(page: Int) { genresRepo.insertAll(page) }
    fun insertAllNewBooks(page: Int) { newBooksRepo.insertAll(page) }
    fun insertAllPublishers() { publishersRepo.insertAll() }
    fun insertAllCollections() { collectionsRepo.insertAll() }
    fun insertAllWishlist() { wishlistRepo.insertAll() }
    fun insertAllRecommendedBooks() { recommendedBooksRepo.insertAll() }
    fun insertAllFreeBooks() { freeBooksRepo.insertAll() }
    fun insertAllPaidBooks() { paidBooksRepo.insertAll() }
    fun insertAllRatedBooks() { ratedBooksRepo.insertAll() }
}