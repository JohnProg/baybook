package com.kitobim.data.local.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.kitobim.data.local.database.dao.*
import com.kitobim.data.local.database.entity.*


@Database(entities = [
    BookEntity::class,
    LibraryEntity::class,
    AuthorEntity::class,
    GenreEntity::class,
    CollectionEntity::class,
    PublisherEntity::class,
    NewBooksEntity::class],
        version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun libraryDao(): LibraryDao
    abstract fun authorDao(): AuthorDao
    abstract fun genreDao(): GenreDao
    abstract fun publisherDao(): PublisherDao
    abstract fun collectionDao(): CollectionDao
    abstract fun newBookDao(): NewBooksDao
    abstract fun bookDao(): BookDao

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