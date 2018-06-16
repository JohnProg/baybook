package com.kitobim.ui.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.kitobim.R
import com.kitobim.data.local.preference.PreferenceHelper
import com.kitobim.data.model.Login
import com.kitobim.data.remote.ApiService
import com.kitobim.data.remote.RetrofitClient
import com.kitobim.ui.activity.AuthenticationActivity
import com.kitobim.ui.custom.AuthResponseListener
import com.kitobim.ui.custom.AuthenticationListener
import com.kitobim.util.Constants
import com.kitobim.viewmodel.ConnectionLiveData
import kotlinx.android.synthetic.main.fragment_login.view.*



class LoginPagerFragment @SuppressLint("ValidFragment") private constructor() : Fragment(),
        View.OnClickListener {

    companion object {
        fun newInstance(): Fragment = LoginPagerFragment()
    }

    private val TOOLBAR_NAVIGATION_ID = -1

    private lateinit var mView: View
    private lateinit var mPagerAdapter: PagerAdapter
    private lateinit var mService: ApiService
    private lateinit var mPreference: SharedPreferences
    private var mFragment: Fragment? = null
    private var mCurrentPage = 0

    private var mEmail = ""
    private var mPhone = ""
    private var mPasswordPhone = ""
    private var mPasswordEmail = ""

    private var isValidEmail = false
    private var isValidNumber = false
    private var isValidPasswordEmail = false
    private var isValidPasswordNumber = false
    private var hasNetwork = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)

        mService = RetrofitClient.getService()
        mPreference = PreferenceHelper.defaultPrefs(context!!)

        setupUi(mView.layout_parent_login_pager)
        initViews()

        val connectionObserver = ConnectionLiveData(context!!)
        connectionObserver.observe(this, Observer {
            hasNetwork = it!!.isConnected
            if (hasNetwork) {
                mView.img_warning_login.visibility = View.GONE
                mView.txt_warning_login.visibility = View.GONE
                changeFabState()
            } else {
                mView.img_warning_login.visibility = View.VISIBLE
                mView.txt_warning_login.visibility = View.VISIBLE
                changeFabState()
            }
        })
        (activity as AuthenticationActivity).setResponseListener(object: AuthResponseListener{
            override fun onError() {
                mView.progress_bar_login.visibility = View.GONE
                mView.txt_progress_login.visibility = View.GONE
                Toast.makeText(activity, "User not found", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                mView.progress_bar_login.visibility = View.GONE
                mView.txt_progress_login.visibility = View.GONE
            }

        })
        return mView
    }

    override fun onClick(v: View) {
        hideSoftKeyboard()
        when (v.id) {
            R.id.btn_forgot_password -> {
                val emailOrPhone = if (mCurrentPage == 0) mEmail else "${mPhone.substring(4)}"
                mFragment = ForgotPasswordFragment.newInstance()
                val bundle = Bundle()
                bundle.putBoolean("is_email", mCurrentPage == 0)
                bundle.putString(Constants.EMAIL_OR_PHONE, emailOrPhone)
                mFragment!!.arguments = bundle
                changeFragment {
                    add(R.id.fragment_container_auth, mFragment).addToBackStack(null)
                }
            }
            TOOLBAR_NAVIGATION_ID -> {
                activity?.onBackPressed()
            }
            R.id.fab_login -> login()
        }
    }

    fun onTextChanged(email_or_phone: String, password: String, isValidUsername: Boolean, isValidPassword: Boolean) {
        when (mView.vp_login.currentItem) {
            0 -> {
                mEmail = email_or_phone
                mPasswordEmail = password
                isValidEmail = isValidUsername
                isValidPasswordEmail = isValidPassword
                changeFabState()
            }
            1 -> {
                mPhone = email_or_phone
                mPasswordPhone = password
                isValidNumber = isValidUsername
                isValidPasswordNumber = isValidPassword
                changeFabState()
            }
        }
    }

    fun changeCurrentPage(item: Int) {
        mView.vp_login.currentItem = item
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

    private fun initViews() {
        mPagerAdapter = PagerAdapter(fragmentManager!!)
        mView.vp_login.adapter = mPagerAdapter

        mView.vp_login.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mCurrentPage = position
                changeFabState()
            }
        })

        mView.toolbar_login.setNavigationOnClickListener(this)
        mView.btn_forgot_password.setOnClickListener(this)
        mView.fab_login.setOnClickListener(this)
    }

    private fun changeFabState(){
        if (isValidEmail && isValidPasswordEmail && mCurrentPage == 0 && hasNetwork||
                isValidNumber && isValidPasswordNumber && mCurrentPage == 1 && hasNetwork) {
            mView.fab_login.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_active_dark)

        } else {
            mView.fab_login.backgroundTintList =
                    ContextCompat.getColorStateList(context!!,R.color.icon_inactive_dark)
        }
    }

    @SuppressLint("ShowToast")
    fun login() {
        if (isValidEmail && isValidPasswordEmail && mCurrentPage == 0 && hasNetwork ||
                isValidNumber && isValidPasswordNumber && mCurrentPage == 1 && hasNetwork) {
            mView.progress_bar_login.visibility = View.VISIBLE
            mView.txt_progress_login.visibility = View.VISIBLE

            val login = if (mCurrentPage == 0) {
                Login(mEmail, mPasswordEmail)
            } else {
                Login(mPhone, mPasswordPhone)
            }

            val listener: AuthenticationListener = activity!! as AuthenticationListener
            listener.onLogin(login)

//            mService.login(login)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            { onSuccess ->
//                                mView.progress_bar_login.visibility = View.GONE
//                                mView.txt_progress_login.visibility = View.GONE
//                                val token = onSuccess.token
//                                Log.i("tag", "login token: $token")
//
//                                mPreference[Constants.TOKEN] = token ?: ""
//                                mPreference[Constants.EMAIL_OR_PHONE] = login.email_or_phone
//                                mPreference[Constants.PASSWORD] = login.password
//                                mPreference[Constants.IS_ACTIVE] = true
//
//                                val intent = Intent(activity!!, MainActivity::class.java)
//                                startActivity(intent)
//
//                                activity?.finish()
//                            },
//                            { onFailure ->
//                                mView.progress_bar_login.visibility = View.GONE
//                                mView.txt_progress_login.visibility = View.GONE
//                                Toast.makeText(context!!, "Invalid email_or_phone or password", Toast.LENGTH_SHORT)
//                                Log.i("tag", "Failure login ${onFailure.message}")
//                            }
//                    )
        }
    }

    private inline fun changeFragment(code: FragmentTransaction.() -> Unit) {
        if (mFragment != null) {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.code()
            transaction.commit()
        }
    }

    private fun hideSoftKeyboard() {
        if (activity!!.currentFocus != null) {
            val inputMethodManager = activity!!
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus.windowToken, 0)
        }
    }

    class PagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        // Returns total number of pages
        override fun getCount(): Int {
            return NUM_ITEMS
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> LoginEmailFragment.newInstance()
                1 -> LoginPhoneFragment.newInstance()
                else -> null
            }
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }

        companion object {
            private const val NUM_ITEMS = 2
        }
    }
}

