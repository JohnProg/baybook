package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.kitobim.R
import com.kitobim.util.TextValidator
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterOptionalFragment @SuppressLint("ValidFragment") private constructor() : Fragment(), TextWatcher {

    companion object {
        fun newInstance(): Fragment = RegisterOptionalFragment()
    }

    private lateinit var mView: View
    private lateinit var mUsername: String
    private lateinit var mEmail: String
    private lateinit var mPassword: String
    private var isValidUsername = false
    private var isValidEmail = false
    private var isValidPassword = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_register_optional, container, false)

        mView.fab_register.setOnClickListener { register() }
        mView.toolbar_register.setNavigationOnClickListener { activity!!.onBackPressed() }

        getTextEditors().forEach { it.addTextChangedListener(this) }

        mView.field_password_register.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) register()
            EditorInfo.IME_ACTION_GO == actionId
        }

        return mView
    }

    override fun afterTextChanged(s: Editable?) { }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (mView.field_username_register.isFocused) {
            mUsername = mView.field_username_register.text.toString()
            isValidUsername = TextValidator.isUsername(mUsername)
        }
        if (mView.field_email_register.isFocused) {
            mEmail = mView.field_email_register.text.toString()
            isValidUsername = TextValidator.isEmail(mEmail)
        }
        if (mView.field_password_register.isFocused) {
            mPassword = mView.field_password_register.text.toString()
            isValidUsername = TextValidator.isPassword(mPassword)
        }
    }

    private fun getTextEditors()  = arrayOf(mView.field_username_register,
            mView.field_email_register, mView.field_password_register)

    private fun register() {

    }
}