package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.kitobim.R
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.model.Login
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.AuthenticationListener
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.util.TextValidator
import com.kitobim.viewmodel.ConnectionLiveData
import kotlinx.android.synthetic.main.fragment_login.view.*

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

        initViews()

        val connectionObserver = ConnectionLiveData(context!!)
        connectionObserver.observe(this, Observer {
            if (it!!.isConnected) {
                mView.img_warning_login.visibility = View.GONE
            } else {
                mView.img_warning_login.visibility = View.VISIBLE
            }
        })

        return mView
    }


    override fun onClick(v: View) {
        hideSoftKeyboard()
        when (v.id){
            R.id.fab_login -> login()

            R.id.btn_forgot_password -> {
                mFragment = ChangePasswordFragment.newInstance()
                changeFragment {
                    add(R.id.fragment_container_auth, mFragment).addToBackStack(null)
                }
            }

            TOOLBAR_NAVIGATION_ID -> {
                activity?.onBackPressed()
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (mView.field_email_login.isFocused) {
            mEmail = s.toString()
            isValidEmail = TextValidator.isEmail(mEmail)
        }

        else if (mView.field_password_login.isFocused) {
            mPassword = s.toString()
            isValidPassword = TextValidator.isPassword(mPassword)

            if (isValidPassword) {
                mView.label_password_login.error = null
                mView.field_password_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.text_primary_dark))
            }
            else {
                mView.label_password_login.error = resources.getString(R.string.error_password)
                mView.field_password_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.error))
            }
        }

        if (isValidEmail && isValidPassword) {
            mView.fab_login.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_active_dark)

        } else {
            mView.fab_login.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_inactive_dark)
        }
    }

    private fun initViews() {
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
                mView.label_email_login.error = null
                mView.field_email_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.text_primary_dark))
            }
            else {
                mView.label_email_login.error = resources.getString(R.string.error_invalid_email)
                mView.field_email_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.error))
            }
        }

    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }

    private fun login() {
        if (isValidEmail && isValidPassword) {
            val login = Login(mEmail, mPassword)

            val listener: AuthenticationListener = activity!! as AuthenticationListener
            listener.onLogin(login)

            Log.i("tag", "login clicked")
        }
    }

    private fun hideSoftKeyboard() {
        if (activity!!.currentFocus != null) {
            val inputMethodManager = activity!!
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus.windowToken, 0)
        }
    }
}

