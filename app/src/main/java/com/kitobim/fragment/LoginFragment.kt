package com.kitobim.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.kitobim.Constants
import com.kitobim.PreferenceHelper
import com.kitobim.PreferenceHelper.set
import com.kitobim.R
import com.kitobim.TextValidator
import com.kitobim.data.model.Login
import com.kitobim.data.model.User
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import kotlinx.android.synthetic.main.fragment_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response





class LoginFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener, TextWatcher {

    companion object {
        fun newInstance(): Fragment = LoginFragment()
    }

    private val TOOLBAR_NAVIGATION_ID = -1

    private lateinit var mView: View
    private lateinit var mPreference: SharedPreferences
    private lateinit var mService: ApiService
    private lateinit var mEmail: String
    private lateinit var mPhone: String
    private lateinit var mPassword: String

    private var mFragment: Fragment? = null
    private var isValidEmail = false
    private var isValidNumber = false
    private var isValidPassword = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)

        mPreference = PreferenceHelper.defaultPrefs(context!!)
        mService = RetrofitClient.getService()

        mView.toolbar_login.setNavigationOnClickListener(this)
        mView.btn_forgot_password.setOnClickListener(this)
        mView.fab_login.setOnClickListener(this)

        mView.field_email_login.addTextChangedListener(this)
        mView.field_password_login.addTextChangedListener(this)

        mView.field_password_login.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) login()
            EditorInfo.IME_ACTION_GO == actionId
        }

        mView.field_email_login.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus || isValidEmail) {
                mView.til_email_login.error = null
                mView.field_email_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.text_primary))
            }
            else {
                mView.til_email_login.error = resources.getString(R.string.error_invalid_email)
                mView.field_email_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.error))
            }
        }

        return mView
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.fab_login -> login()

            R.id.btn_forgot_password -> {
                mFragment = ChangePasswordFragment.newInstance()
                changeFragment {
                    add(R.id.fragment_container, mFragment).addToBackStack(null)
                }
            }

            TOOLBAR_NAVIGATION_ID -> {
                hideSoftKeyboard()
                activity!!.onBackPressed()
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (mView.field_email_login.isFocused) {
            mEmail = s.toString()
            isValidEmail = TextValidator.isValidEmail(mEmail)
        }

        else if (mView.field_password_login.isFocused) {
            mPassword = s.toString()
            isValidPassword = TextValidator.isValidPassword(mPassword)

            if (isValidPassword) {
                mView.til_password_login.error = null
                mView.field_password_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.text_primary))
            }
            else {
                mView.til_password_login.error = resources.getString(R.string.error_password)
                mView.field_password_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.error))
            }
        }

        if (isValidEmail && isValidPassword) {
            mView.fab_login.apply {
                isClickable = true
                backgroundTintList = ContextCompat.getColorStateList(context!!,R.color.primary)
            }
        } else {
            mView.fab_login.apply {
                isClickable = false
                backgroundTintList = ContextCompat.getColorStateList(context!!,R.color.grey300)
            }
        }
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
        hideSoftKeyboard()
    }

    private fun login() {
        val login = Login(mEmail, mPassword)
        val result = mService.login(login)

        result.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token

                    mPreference[Constants.TOKEN] = token
                    mPreference[Constants.USERNAME] = mEmail
                    mPreference[Constants.PASSWORD] = mPassword
                    mPreference[Constants.IS_ACTIVE] = true

                    mFragment = MainFragment.newInstance()

                    changeFragment {
                        replace(R.id.fragment_container, mFragment)
                    }
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) { }
        })
    }

    private fun hideSoftKeyboard() {
        if (activity!!.currentFocus != null) {
            val inputMethodManager = activity!!
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus.windowToken, 0)
        }
    }
}

