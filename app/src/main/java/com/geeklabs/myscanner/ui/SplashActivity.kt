package com.geeklabs.myscanner.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geeklabs.myscanner.R
import com.geeklabs.myscanner.extensions.launchActivity
import com.geeklabs.myscanner.extensions.withDelay

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        withDelay(2000) {
            if (!isFinishing) {
                launchActivity<MainActivity>()
                finish()
            }
        }
    }
}