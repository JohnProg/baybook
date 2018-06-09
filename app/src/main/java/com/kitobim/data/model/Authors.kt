package com.kitobim.data.model

import com.kitobim.data.local.database.entity.AuthorEntity

class Authors(
        val data: List<AuthorEntity>,
        val meta: Meta
)