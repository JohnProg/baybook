package com.kitobim

import com.kitobim.data.model.Login

interface AuthenticationListener {
    fun onLogin(login: Login)
    fun onRegister()
}

