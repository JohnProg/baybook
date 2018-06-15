package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.EditText
import com.kitobim.R
import com.kitobim.util.Constants
import com.kitobim.util.TextValidator
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*

class ForgotPasswordFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        TextWatcher {

    companion object {
        fun newInstance(): Fragment = ForgotPasswordFragment()
    }

    private var mFragment: Fragment? = null
    private var mEmail: String = ""
    private var isValidEmail = false
    private var mEmailOrPhone = ""
    private var isEmailPage = true

    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        mEmailOrPhone = arguments!!.getString(Constants.EMAIL_OR_PHONE)
        isEmailPage = arguments!!.getBoolean("is_email")

        initViews()

        setupUi(mView.layout_parent_forgot_password)

        mView.field_email_forgot_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) sendRequest()
            EditorInfo.IME_ACTION_GO == actionId
        }
        return mView
    }


    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mEmail = s.toString()
        isValidEmail = TextValidator.isEmail(mEmail)
        if (isValidEmail) {
            mView.fab_change_password.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_active_dark)

        } else {
            mView.fab_change_password.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_inactive_dark)

        }
    }

    @SuppressLint("ResourceType")
    private fun initViews() {
        if (isEmailPage) {
            mView.label_email_forgot_password.visibility = View.VISIBLE
            mView.subtitle_email_forgot_password.visibility = View.VISIBLE
            mView.label_phone_forgot_password.visibility = View.GONE
            mView.subtitle_phone_forgot_password.visibility = View.GONE

            mView.field_email_forgot_password.setText(mEmailOrPhone)
            mView.field_email_forgot_password.addTextChangedListener(this)
        } else {
            mView.label_email_forgot_password.visibility = View.GONE
            mView.subtitle_email_forgot_password.visibility = View.GONE
            mView.label_phone_forgot_password.visibility = View.VISIBLE
            mView.subtitle_phone_forgot_password.visibility = View.VISIBLE

            mView.field_phone_forgot_password.setText(mEmailOrPhone)
            mView.field_phone_forgot_password.addTextChangedListener(this)
        }

        mView.fab_change_password.setOnClickListener{ sendRequest() }
        mView.toolbar_change_password.setNavigationOnClickListener{ activity?.onBackPressed() }
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

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }
    private fun sendRequest() {

    }

    private fun hideSoftKeyboard() {
        if (activity!!.currentFocus != null) {
            val inputMethodManager = activity!!
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus.windowToken, 0)
        }
    }
}
