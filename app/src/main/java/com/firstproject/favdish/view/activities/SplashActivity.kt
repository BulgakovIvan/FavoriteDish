package com.firstproject.favdish.view.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.ActivitySplashBinding
import com.firstproject.favdish.utils.MAIN_ACTIVITY_DELAY
import com.firstproject.favdish.utils.SPLASH_ANIMATION_DURATION

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashBinding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        splashAnimation.duration = SPLASH_ANIMATION_DURATION

        splashAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    },
                    MAIN_ACTIVITY_DELAY
                )
            }

            override fun onAnimationRepeat(animation: Animation?) {}

        })

        splashBinding.tvAppName.animation = splashAnimation
    }
}