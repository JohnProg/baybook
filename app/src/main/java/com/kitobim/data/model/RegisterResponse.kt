package com.kitobim.data.model

class RegisterResponse(
        val success: List<String> = emptyList(),
        val error: List<String> = emptyList()
)