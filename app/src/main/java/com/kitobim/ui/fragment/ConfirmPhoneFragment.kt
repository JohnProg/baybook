package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.kitobim.R
import com.kitobim.ui.activity.AuthenticationActivity
import com.kitobim.ui.custom.AuthResponseListener
import com.kitobim.ui.custom.AuthenticationListener
import com.kitobim.util.TextValidator
import kotlinx.android.synthetic.main.fragment_confirm_phone.*
import kotlinx.android.synthetic.main.fragment_confirm_phone.view.*

class ConfirmPhoneFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        TextWatcher {

    companion object {
        fun newInstance() =  ConfirmPhoneFragment()

    }

    private var mCode: String = ""
    private var isValidCode = false
    private var mPhone = ""

    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_confirm_phone, container, false)

        mPhone = arguments!!.getString("phone")

        mView.subtitle_confirm_phone.text = getString(R.string.subtitle_confirm_phone, mPhone)

        setupUi(mView.layout_parent_confirm_phone)

        mView.fab_confirm_phone.setOnClickListener{ sendCode() }
        mView.field_code_confirm_phone.addTextChangedListener(this)

        mView.field_code_confirm_phone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) sendCode()
            EditorInfo.IME_ACTION_GO == actionId
        }

        (activity as AuthenticationActivity).setResponseListener(object: AuthResponseListener{
            override fun onError() {
                mView.progress_bar_confirm.visibility = View.GONE
                mView.txt_progress_confirm.visibility = View.GONE
            }

            override fun onSuccess() {
                mView.progress_bar_confirm.visibility = View.GONE
                mView.txt_progress_confirm.visibility = View.GONE
                Toast.makeText(activity, R.string.error_cofirm_code, Toast.LENGTH_SHORT).show()
            }

        })
        return mView
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).setSupportActionBar(toolbar_confirm_phone)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mCode = s.toString()
        isValidCode = TextValidator.isVerificationCode(mCode)
        if (isValidCode) {
            mView.fab_confirm_phone.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_active_dark)

        } else {
            mView.fab_confirm_phone.backgroundTintList =
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

    private fun hideSoftKeyboard() {
        if (activity!!.currentFocus != null) {
            val inputMethodManager = activity!!
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus.windowToken, 0)
        }
    }

    private fun sendCode() {
        if (isValidCode) {
            mView.progress_bar_confirm.visibility = View.VISIBLE
            mView.txt_progress_confirm.visibility = View.VISIBLE
            (activity as AuthenticationListener).onConfirm(mPhone, mCode)
        }
    }
}
