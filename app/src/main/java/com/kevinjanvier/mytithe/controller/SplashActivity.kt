package com.kevinjanvier.mytithe.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kevinjanvier.mytithe.R

class SplashActivity : AppCompatActivity() {

    val time_out = 2500


    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({
            if (App.prefs.islogIn){
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                finish()
            }else{
                startActivity(Intent(this@SplashActivity, Intro::class.java))
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                finish()
            }
        }, time_out.toLong())
    }
}
