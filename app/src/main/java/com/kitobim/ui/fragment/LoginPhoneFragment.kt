package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.kitobim.R
import com.kitobim.util.TextValidator
import kotlinx.android.synthetic.main.fragment_login_phone.view.*

class LoginPhoneFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        TextWatcher {

    companion object {
        fun newInstance(): Fragment = LoginPhoneFragment()
    }

    private lateinit var mView: View
    private lateinit var mPagerFragment: LoginPagerFragment

    private var mPassword = ""
    private var mNumber = ""
    private var isValidNumber = false
    private var isValidPassword = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_login_phone, container, false)
        mPagerFragment = fragmentManager!!
                .findFragmentById(R.id.fragment_container_auth) as LoginPagerFragment
        initViews()
        return mView
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (mView.field_phone_login.isFocused) {
            mNumber = s.toString()
            isValidNumber = TextValidator.isPhoneNumber(mPassword)
        }

        else if (mView.field_password_phone_login.isFocused) {
            mPassword = s.toString()
            isValidPassword = TextValidator.isPassword(mPassword)

            if (isValidPassword) {
                mView.label_password_phone_login.error = null
                mView.field_password_phone_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.text_primary_dark))
            }
            else {
                mView.label_password_phone_login.error = resources.getString(R.string.error_password)
                mView.field_password_phone_login.setTextColor(ContextCompat
                        .getColor(context!!,R.color.error))
            }
        }
        mPagerFragment.onTextChanged(mNumber, mPassword, isValidNumber, isValidPassword)
    }

    private fun initViews() {
        mView.btn_go_email.setOnClickListener{
            mPagerFragment.changeCurrentPage(0)
        }

        mView.field_phone_login.addTextChangedListener(this)
        mView.field_password_phone_login.addTextChangedListener(this)

        mView.field_password_phone_login.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) mPagerFragment.login()
            EditorInfo.IME_ACTION_GO == actionId
        }

        mView.field_phone_login.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus || isValidNumber) {
                mView.label_phone_login.error = null
                mView.field_phone_login.setTextColor(ContextCompat
                        .getColor(context!!, R.color.text_primary_dark))
            }
            else {
                mView.label_phone_login.error = resources.getString(R.string.error_invalid_email)
                mView.field_phone_login.setTextColor(ContextCompat
                        .getColor(context!!, R.color.error))
            }
        }
    }
}

