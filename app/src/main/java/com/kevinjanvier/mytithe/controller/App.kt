package com.kevinjanvier.mytithe.controller

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.kevinjanvier.mytithe.R
import com.kevinjanvier.mytithe.utils.SharePref

/**
 * Created by kevinjanvier on 20/01/2018.
 */
class App : Application() {

    companion object {
        lateinit var prefs: SharePref
    }

    override fun onCreate() {
        prefs = SharePref(applicationContext)
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.ad_unit))
//        Stetho.initializeWithDefaults(this)

    }
}