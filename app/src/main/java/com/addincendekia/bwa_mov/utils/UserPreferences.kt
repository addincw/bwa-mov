package com.addincendekia.bwa_mov.utils

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(val context: Context) {
    companion object{
        val USER_PREF = "USER_PREF"
    }

    private var sharedPref = context.getSharedPreferences(USER_PREF, 0)
    private val accessPref: SharedPreferences.Editor = sharedPref.edit()

    fun setValue(key: String, value: String) {
        accessPref.putString(key, value)
        accessPref.apply()
    }
    fun setValue(key: String, value: Set<String>) {
        accessPref.putStringSet(key, value)
        accessPref.apply()
    }

    fun getValue(key: String): String? {
        return sharedPref.getString(key, "")
    }
    fun getValue(key: String, type: String = "string"): Set<String>? {
        return sharedPref.getStringSet(key, setOf())
    }

    fun removeValue(key: String) {
        accessPref.remove(key)
        accessPref.apply()
    }

    fun reset() {
        sharedPref.edit().clear().commit()
    }
}