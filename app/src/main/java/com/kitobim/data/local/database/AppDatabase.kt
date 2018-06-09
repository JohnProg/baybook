package com.kitobim.data.local.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.kitobim.data.local.database.coverter.DataConverter
import com.kitobim.data.local.database.dao.*
import com.kitobim.data.local.database.entity.*


@Database(entities = [
    AuthorEntity::class,
    AuthorListEntity::class,
    GenreEntity::class,
    GenreListEntity::class,
    CollectionEntity::class,
    PublisherEntity::class,
    BookEntity::class,
    LibraryEntity::class,
    WishlistEntity::class,
    RecommendedBooksEntity::class,
    NewBooksEntity::class,
    FreeBooksEntity::class,
    PaidBooksEntity::class,
    RatedBooksEntity::class],
        version = 1, exportSchema = false)

@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun authorDao(): AuthorDao
    abstract fun authorListDao(): AuthorListDao
    abstract fun genreDao(): GenreDao
    abstract fun genreListDao(): GenreListDao
    abstract fun publisherDao(): PublisherDao
    abstract fun collectionDao(): CollectionDao
    abstract fun bookDao(): BookDao
    abstract fun libraryDao(): LibraryDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun recommendedBooksDao(): RecommendedBooksDao
    abstract fun newBooksDao(): NewBooksDao
    abstract fun freeBooksDao(): FreeBooksDao
    abstract fun paidBooksDao(): PaidBooksDao
    abstract fun ratedBooksDao(): RatedBooksDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "kitobim_db").build()
            }
            return INSTANCE as AppDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}