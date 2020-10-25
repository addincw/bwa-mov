package com.addincendekia.bwa_mov.utils

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(val context: Context) {
    companion object{
        val USER_PREF = "USER_PREF"
    }

    var sharedPref = context.getSharedPreferences(USER_PREF, 0)

    fun setValue(key: String, value: String) {
        val accessPref: SharedPreferences.Editor = sharedPref.edit()

        accessPref.putString(key, value)
        accessPref.apply()
    }

    fun getValue(key: String): String? {
        return sharedPref.getString(key, "")
    }

    fun reset() {
        sharedPref.edit().clear().commit()
    }
}