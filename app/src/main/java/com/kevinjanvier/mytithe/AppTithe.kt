package com.kevinjanvier.mytithe

import android.app.Application
import com.google.android.gms.ads.MobileAds

/**
 * Created by kevinjanvier on 09/10/2017.
 */

class AppTithe : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.ad_unit))
    }
}
