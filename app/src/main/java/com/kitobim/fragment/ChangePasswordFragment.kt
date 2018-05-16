package com.kitobim.fragment

import android.annotation.SuppressLint
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
import com.kitobim.R
import com.kitobim.TextValidator
import kotlinx.android.synthetic.main.fragment_change_password.view.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class ChangePasswordFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener, TextWatcher {

    companion object {
        fun newInstance(): Fragment = ChangePasswordFragment()
    }

    private val TOOLBAR_NAVIGATION_ID = -1
    private var mFragment: Fragment? = null
    private var mEmail: String = ""
    private var isValidEmail = false

    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false)

        mView.fab_send_request.setOnClickListener(this)
        mView.toolbar_change_password.setNavigationOnClickListener(this)
        mView.field_email_change_password.addTextChangedListener(this)

        mView.field_email_change_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) sendRequest()
            EditorInfo.IME_ACTION_GO == actionId
        }
        return mView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_send_request -> sendRequest()
            TOOLBAR_NAVIGATION_ID -> activity!!.onBackPressed()
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mEmail = s.toString()
        isValidEmail = TextValidator.isEmail(mEmail)
        if (isValidEmail) {
            mView.fab_login.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_active_dark)

        } else {
            mView.fab_login.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_inactive_dark)

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

}
