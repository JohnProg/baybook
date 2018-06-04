package com.kitobim.data.model


open class Book(
        val id: Int,
        val title: String,
        val thumbnail: String?,
        val cover: String? = null,
        val price: Int,
        val rating: Float = 0F,
        val year: String? = null,
        val annotation: String = "",
        val authors: List<String>,
        val genres: List<String> = emptyList(),
        val isbn: String? = null,
        val in_wishlist: Boolean = false,
        val has_sample: Boolean = false,
        val is_purchased: Boolean = false
)

