package com.kitobim.data.model

class Login(
        val email_phone: String,
        val password: String,
        val serial_number: String = " ",
        val name: String = " "
)