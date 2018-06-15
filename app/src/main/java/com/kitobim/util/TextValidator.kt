package com.kitobim.util

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

object TextValidator {
    fun isUsername(username: String): Boolean {
        return  !TextUtils.isEmpty(username) && username.length >= Constants.USERNAME_MIN_LENGTH
    }

    fun isEmail(email: String) : Boolean {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        return false
    }

    fun isPhone(phone: String): Boolean {
        if (!TextUtils.isEmpty(phone)) {
            return Pattern.compile("^[+]?(998)[0-9]{9}").matcher(phone).matches()
        }
        return false
    }

    fun isPassword(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= Constants.PASSWORD_MIN_LENGTH
    }

    fun isEmailOrPhone(emailOrPhone: String): Boolean {
        return isEmail(emailOrPhone) || isPhone(emailOrPhone)
    }

    fun isVerificationCode(code: String): Boolean {
        if (code.length == Constants.VERIFICATION_CODE_LENGTH) {
            for (char in code) {
                if (!char.isDigit()) return false
            }
            return true
        }
        return false
    }
}