package com.kitobim.fragment

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.kitobim.Constants
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.set
import com.kitobim.R
import com.kitobim.data.model.Login
import com.kitobim.data.model.User
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener{

    companion object {
        fun newInstance(): Fragment = LoginFragment()
    }

    private lateinit var mParent: View
    private lateinit var mPreference: SharedPreferences
    private lateinit var mService: ApiService
    private var mFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mParent = inflater.inflate(R.layout.fragment_login, container, false)

        mPreference = PreferenceHelper.defaultPrefs(context!!)
        mService = RetrofitClient.getService()

        getButtons().forEach { it.setOnClickListener(this) }

        return mParent
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> login()

            R.id.btn_forgot_password -> {
                mFragment = ChangePasswordFragment.newInstance()

                fragmentManager!!
                        .beginTransaction()
                        .replace(R.id.fragment_container, mFragment)
                        .addToBackStack(null)
                        .commit()
            }

            R.id.btn_login_later -> {
                mFragment = MainFragment.newInstance()
                changeFragment()
            }
        }
    }

    private fun getButtons()
            = arrayOf(mParent.btn_login, mParent.btn_login_later, mParent.btn_forgot_password)

    private fun login(){
        val username = field_username.text.toString()
        val password = field_password.text.toString()

        val login = Login(username, password)
        val result = mService.login(login)

        result.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful) {
                    val token = response.body()?.token

                    mPreference[Constants.TOKEN] = token
                    mPreference[Constants.USERNAME] = username
                    mPreference[Constants.PASSWORD] = password
                    mPreference[Constants.IS_ACTIVE] = true

                    mFragment = MainFragment.newInstance()

                    changeFragment()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        })

    }

    private fun changeFragment() {
        if (mFragment != null) {
            hideSoftKeyboard()

            fragmentManager!!
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .commit()

            hideSoftKeyboard()
        }
    }

    private fun hideSoftKeyboard() {
        if (activity!!.currentFocus != null) {
            val inputMethodManager = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus.windowToken, 0)
        }
    }


}
