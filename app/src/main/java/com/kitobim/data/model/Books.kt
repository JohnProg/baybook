package com.kitobim.data.model

import com.kitobim.data.local.database.entity.BookEntity

class Books(
        val data: List<BookEntity>,
        val meta: Meta
)