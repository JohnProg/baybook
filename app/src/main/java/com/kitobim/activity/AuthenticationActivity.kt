package com.kitobim.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kitobim.R
import com.kitobim.fragment.WelcomeFragment



class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (savedInstanceState == null) {
            val fragment = WelcomeFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_auth, fragment)
            transaction.commit()
        }
    }
}
