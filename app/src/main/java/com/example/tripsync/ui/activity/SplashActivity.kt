package com.example.tripsync.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.tripsync.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.splash_effect)
        val splashImage = findViewById<ImageView>(R.id.splash_image)
        splashImage.startAnimation(anim)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}