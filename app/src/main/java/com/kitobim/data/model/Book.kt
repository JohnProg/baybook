package com.kitobim.data.model

class Book(
        val id: String,
        val title: String,
        val authors: List<String>,
        val categories: List<String>,
        val cover: String,
        val price: String,
        val purchased: Boolean,
        val has_sample: Boolean
)