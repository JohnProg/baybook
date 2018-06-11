package com.kitobim.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.kitobim.R
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.set
import com.kitobim.data.model.Login
import com.kitobim.data.model.Register
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.ui.custom.AuthenticationListener
import com.kitobim.ui.fragment.WelcomeFragment
import com.kitobim.util.Constants
import com.kitobim.util.LocaleHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


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



//        call.enqueue(object : Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                Log.i("tag", "login response: ${response.code()}")
//                if (response.isSuccessful) {
//
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.i("tag", "login failure")
//            }
//        })
    }

    override fun onRegister(register: Register) {
        mService.register(register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess ->
                            val token = onSuccess.token

                            mPreference[Constants.TOKEN] = token ?: ""
                            mPreference[Constants.EMAIL_PHONE] = register.email
                            mPreference[Constants.PASSWORD] = register.password
                            mPreference[Constants.IS_ACTIVE] = true

                            val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                            startActivity(intent)

                            finish()
                        },
                        { onFailure ->
                            Log.i("tag", "Failure recommended books ${onFailure.message}")
                        }
                )

//        call.enqueue(object : Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                Log.i("tag", "register response: ${response.code()}")
//                if (response.isSuccessful) {
//
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.i("tag", "register failure")
//            }
//        })
    }
}
