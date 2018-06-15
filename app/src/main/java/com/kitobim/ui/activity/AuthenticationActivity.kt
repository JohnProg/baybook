package com.kitobim.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kitobim.R
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.local.preference.PreferenceHelper.set
import com.kitobim.data.model.Login
import com.kitobim.data.model.Register
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.ui.custom.AuthenticationListener
import com.kitobim.ui.fragment.ConfirmEmailFragment
import com.kitobim.ui.fragment.ConfirmPhoneFragment
import com.kitobim.ui.fragment.WelcomeFragment
import com.kitobim.util.Constants
import com.kitobim.util.LocaleHelper
import com.kitobim.util.TextValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AuthenticationActivity : AppCompatActivity(), AuthenticationListener {

    private lateinit var mService: ApiService
    private lateinit var mPreference: SharedPreferences
    private var mFragment: Fragment? = null

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

    @SuppressLint("ShowToast")
    override fun onLogin(login: Login) {
        val emailOrPhone = login.email_or_phone
        val password = login.password

        if (TextValidator.isEmail(emailOrPhone)) {
            mService.loginWithEmail(emailOrPhone, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { response ->
                                Log.i("tag", "phone: $emailOrPhone success: ${response.username}")
                                if (response.token.isNotEmpty()) {
                                    mPreference[Constants.TOKEN] = response.token
                                    mPreference[Constants.EMAIL_OR_PHONE] = login.email_or_phone
                                    mPreference[Constants.PASSWORD] = login.password
                                    mPreference[Constants.IS_ACTIVE] = true

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)

                                    finish()
                                }
                            },
                            { onFailure ->
                                Toast.makeText(this, "Invalid email_or_phone or password", Toast.LENGTH_SHORT)
                                Log.i("tag", "Failure login ${onFailure.message}")
                            }
                    )
        } else {
            val phone = if(emailOrPhone[0] == '+') emailOrPhone.substring(1) else emailOrPhone

            mService.loginWithPhone(phone, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { response ->
                                Log.i("tag", "phone: $phone success: ${response.username}")
                                mPreference[Constants.TOKEN] = response.token
                                mPreference[Constants.USERNAME] = response.username
                                mPreference[Constants.EMAIL_OR_PHONE] = login.email_or_phone
                                mPreference[Constants.PASSWORD] = login.password
                                mPreference[Constants.IS_ACTIVE] = true

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)

                                finish()
                            },
                            { onFailure ->
                                Toast.makeText(this, "Invalid email_or_phone or password", Toast.LENGTH_SHORT)
                                Log.i("tag", "Failure phone: $phone ${onFailure.message}")
                            }
                    )
        }


//        call.enqueue(object : Callback<RegisterResponse> {
//            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
//                Log.i("tag", "login response: ${response.code()}")
//                if (response.isSuccessful) {
//
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                Log.i("tag", "login failure")
//            }
//        })
    }

    override fun onRegister(register: Register) {
        val name = register.name
        val emailOrPhone = register.email_or_phone
        val password = register.password

        if (TextValidator.isEmail(emailOrPhone)) {
            mService.registerWithEmail(name, emailOrPhone, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { response ->
                                Log.i("tag", "phone: $emailOrPhone error: ${response.error} success: ${response.success}")
                                mPreference[Constants.EMAIL_OR_PHONE] = emailOrPhone
                                mPreference[Constants.PASSWORD] = password
                                mFragment = ConfirmEmailFragment.newInstance()
                                changeFragment {
                                    replace(R.id.fragment_container_auth, mFragment)
                                }
                            },
                            { onFailure ->
                                Log.i("tag", "Failure register with email ${onFailure.message}")
                            }
                    )
        } else {
            val phone = if(emailOrPhone[0] == '+' && emailOrPhone.length == 13) {
                emailOrPhone.substring(1)
            } else {
                emailOrPhone
            }

            mService.registerWithPhone(name, phone, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { response ->
                                Log.i("tag", "phone: $phone error: ${response.error} success: ${response.success}")
                                mPreference[Constants.EMAIL_OR_PHONE] = phone
                                mPreference[Constants.PASSWORD] = password
                                val bundle = Bundle()
                                bundle.putString("phone", phone)
                                mFragment = ConfirmPhoneFragment.newInstance()
                                mFragment!!.arguments = bundle
                                changeFragment {
                                    replace(R.id.fragment_container_auth, mFragment)
                                }
                            },
                            { onFailure ->
                                Log.i("tag", "Failure register with phone ${onFailure.message}")
                            }
                    )
        }




//        call.enqueue(object : Callback<RegisterResponse> {
//            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
//                Log.i("tag", "register response: ${response.code()}")
//                if (response.isSuccessful) {
//
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                Log.i("tag", "register failure")
//            }
//        })
    }

    override fun onConfirm(phone: String, code: String) {
        mService.confirmPhone(phone, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response ->
                            mPreference[Constants.TOKEN] = response.token
                            mPreference[Constants.USERNAME] = response.username
                            mPreference[Constants.IS_ACTIVE] = true
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        { onFailure ->
                            Log.i("tag", "Failure register with phone ${onFailure.message}")
                        }
                )
    }


    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }
}
