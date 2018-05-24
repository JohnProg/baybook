package com.kitobim.data.model

class Book(
        val id: String,
        val title: String,
        val cover: String?,
        val price: Int,
        val rating: Float,
        val year: String,
        val annotation: String,
        val authors: List<String>,
        val genres: List<String>,
        val isbn: String?,
        val in_wishlist: Boolean,
        val has_sample: Boolean,
        val is_purchased: Boolean
)