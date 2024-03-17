package com.example.weatherforecastapplication.shared

import android.content.Context
import android.content.SharedPreferences

// put shared pref code here...
class Storage (val context: Context){
    private var preferences: SharedPreferences
    = context.getSharedPreferences("en", Context.MODE_PRIVATE)

    fun getPreferredLocale(): String {
        return preferences.getString("preferred_locale", LocaleUtil.OPTION_PHONE_LANGUAGE)!!
    }

    fun setPreferredLocale(localeCode: String) {
        preferences.edit().putString("preferred_locale", localeCode).apply()
    }
}