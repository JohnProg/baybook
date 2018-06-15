package com.kitobim.ui.custom

import com.kitobim.data.model.Login
import com.kitobim.data.model.Register

interface AuthenticationListener {
    fun onLogin(login: Login)
    fun onRegister(register: Register)
    fun onConfirm(phone: String, code: String)
}

