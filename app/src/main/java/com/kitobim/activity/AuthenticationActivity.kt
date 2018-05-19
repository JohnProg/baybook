package com.kitobim.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kitobim.*
import com.kitobim.PreferenceHelper.set
import com.kitobim.data.model.Login
import com.kitobim.data.model.User
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.fragment.WelcomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthenticationActivity : AppCompatActivity(), AuthenticationListener {

    private lateinit var mService: ApiService
    private lateinit var mPreference: SharedPreferences

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        mService = RetrofitClient.getService()
        mPreference = PreferenceHelper.defaultPrefs(this)

        mPreference[Constants.IS_NEWBIE] = false

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (savedInstanceState == null) {
            val fragment = WelcomeFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_auth, fragment)
            transaction.commit()
        }
    }

    override fun onLogin(login: Login) {
        val call = mService.login(login)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token

                    mPreference[Constants.TOKEN] = token
                    mPreference[Constants.USERNAME] = login.email_phone
                    mPreference[Constants.PASSWORD] = login.password
                    mPreference[Constants.IS_ACTIVE] = true

                    val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {}
        })
    }

    override fun onRegister() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
