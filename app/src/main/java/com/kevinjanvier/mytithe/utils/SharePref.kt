package com.kevinjanvier.mytithe.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by kevinjanvier on 20/01/2018.
 */
class SharePref(context: Context) {
    val PREF_FILE_NAME = "mytithe"
    val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,0)

    val FIRST_TIME = "firsttime"
    val MY_CURRENCY ="currency"

    //    Check if the User is the FirstTime User
    var islogIn: Boolean
        get() = prefs.getBoolean(FIRST_TIME, false)
        set(value) = prefs.edit().putBoolean(FIRST_TIME, value).apply()


    var myCurrency:String
    get() = prefs.getString(MY_CURRENCY, "")
    set(value) = prefs.edit().putString(MY_CURRENCY, value).apply()





}