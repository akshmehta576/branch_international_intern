package com.practice.branchinternapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.practice.branchinternapp.R
import com.practice.branchinternapp.databinding.ActivitySplashBinding
import com.practice.branchinternapp.util.AuthTokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var tokenManager: AuthTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupStatusBar()
        setupBinding()
        animateLogo()
        setupDelayHandler()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
    // endregion

    // region CUSTOM METHODS
    // To setup Status Bar
    private fun setupStatusBar() {
        //to make status bar color transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    // To setup UI Binding
    private fun setupBinding() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    // To add fade animation
    private fun animateLogo() {
        val animation: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.fade_animation)
        findViewById<ImageView>(R.id.app_icon).startAnimation(animation)
    }

    // To add fade animation
    private fun setupDelayHandler() {
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 3000)
    }
}