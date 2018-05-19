package com.kitobim

object TextValidator {

    fun isEmail(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()


    fun isPassword(password: String) =
            password.isNotEmpty() && password.length >= Constants.PASSWORD_MIN_LENGTH


    fun isUsername(username: String) =
            username.isNotEmpty() && username.length >= Constants.USERNAME_MIN_LENGTH
}