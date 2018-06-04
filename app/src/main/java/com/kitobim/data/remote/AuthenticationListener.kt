package com.kitobim.data.remote

import com.kitobim.data.model.Login
import com.kitobim.data.model.Register

interface AuthenticationListener {
    fun onLogin(login: Login)
    fun onRegister(register: Register)
}

