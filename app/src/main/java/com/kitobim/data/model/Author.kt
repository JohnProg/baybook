package com.kitobim.data.model

class Author(
        val id: Int,
        val name: String,
        val photo: String?,
        val bio: String?,
        val birth_date: String?,
        val death_date: String?,
        val country: String?,
        val books: Int
)