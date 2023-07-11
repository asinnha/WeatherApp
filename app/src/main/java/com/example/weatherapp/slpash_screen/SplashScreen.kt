package com.example.weatherapp.slpash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        GlobalScope.launch {
            delay(1200)
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            this@SplashScreen.finish()
        }
    }
}