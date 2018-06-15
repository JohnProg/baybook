package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.kitobim.R
import com.kitobim.data.model.Register
import com.kitobim.ui.custom.AuthenticationListener
import com.kitobim.util.TextValidator
import kotlinx.android.synthetic.main.fragment_register.view.*


class RegisterFragment @SuppressLint("ValidFragment") private constructor() : Fragment(), TextWatcher {

    companion object {
        fun newInstance(): Fragment = RegisterFragment()
    }

    private lateinit var mView: View
    private var mUsername = ""
    private  var mEmailOrPhone = ""
    private  var mPassword = ""
    private var isValidUsername = false
    private var isValidEmailOrPhone = false
    private var isValidPassword = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_register, container, false)

        setupUi(mView.layout_parent_register)

        mView.fab_register.setOnClickListener { register() }
        mView.toolbar_register.setNavigationOnClickListener { activity!!.onBackPressed() }

        getTextEditors().forEach { it.addTextChangedListener(this) }

        mView.field_username_register.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || isValidUsername) {
                mView.label_username_register.error = null
                mView.field_username_register.setTextColor(ContextCompat
                        .getColor(context!!, R.color.text_primary_dark))
            } else {
                if (mUsername.isEmpty()) {
                    mView.label_username_register.error = resources.getString(R.string.field_is_empty)
                } else {
                    mView.label_username_register.error = resources.getString(R.string.error_username)
                }
                mView.field_username_register.setTextColor(ContextCompat
                        .getColor(context!!, R.color.error))
            }
        }

        mView.field_email_phone_register.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || isValidEmailOrPhone) {
                mView.label_email_phone_register.error = null
                mView.field_email_phone_register.setTextColor(ContextCompat
                        .getColor(context!!, R.color.text_primary_dark))
            } else {
                if (mEmailOrPhone.isEmpty()) {
                    mView.label_email_phone_register.error = resources.getString(R.string.field_is_empty)
                } else {
                    mView.label_email_phone_register.error = resources.getString(R.string.error_email_or_phone)

                }
                mView.field_email_phone_register.setTextColor(ContextCompat
                        .getColor(context!!, R.color.error))
            }
        }

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
        if (mView.field_email_phone_register.isFocused) {
            mEmailOrPhone = mView.field_email_phone_register.text.toString()
            isValidEmailOrPhone = TextValidator.isEmailOrPhone(mEmailOrPhone)
            if (isValidEmailOrPhone) {
                mView.field_email_phone_register
                        .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0)
            } else {
                mView.field_email_phone_register
                        .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            Log.i("tag", "Email or phone: $isValidEmailOrPhone")
        }
        if (mView.field_password_register.isFocused) {
            mPassword = mView.field_password_register.text.toString()
            isValidPassword = TextValidator.isPassword(mPassword)
            if (isValidPassword) {
                mView.label_password_register.error = null
                mView.field_password_register.setTextColor(ContextCompat
                        .getColor(context!!,R.color.text_primary_dark))
            }
            else {
                mView.label_password_register.error = resources.getString(R.string.error_password)
                mView.field_password_register.setTextColor(ContextCompat
                        .getColor(context!!,R.color.error))
            }
        }


        if (isValidUsername && isValidEmailOrPhone && isValidPassword) {
            mView.fab_register.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_active_dark)

        } else {
            mView.fab_register.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_inactive_dark)
        }
    }

    private fun setupUi(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUi(innerView)
            }
        }
    }

    private fun getTextEditors()  = arrayOf(mView.field_username_register,
            mView.field_email_phone_register, mView.field_password_register)

    private fun register() {
        if (isValidUsername && isValidEmailOrPhone && isValidPassword) {

            val register = Register(mUsername, mEmailOrPhone, mPassword)
            (activity as AuthenticationListener).onRegister(register)
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