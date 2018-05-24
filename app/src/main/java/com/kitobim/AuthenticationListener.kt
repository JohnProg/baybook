package com.kitobim

import com.kitobim.data.model.Login
import com.kitobim.data.model.Register

interface AuthenticationListener {
    fun onLogin(login: Login)
    fun onRegister(register: Register)
}

