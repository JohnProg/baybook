package com.kitobim.data.model

import com.kitobim.data.local.database.entity.PublisherEntity

class Publishers(
        val data: List<PublisherEntity>,
        val count: Int
)