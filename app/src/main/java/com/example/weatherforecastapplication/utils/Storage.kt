package com.example.weatherforecastapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class Storage (context: Context?) {

    private var preferences: SharedPreferences = context!!.getSharedPreferences("preferred_locale", Context.MODE_PRIVATE)

    fun getPreferredLocale(): String {
        Log.i("TAG", "getPreferredLocale: " +
                "${preferences.getString("lang", LocaleUtil.OPTION_PHONE_LANGUAGE)!!}")
        return preferences.getString("lang", LocaleUtil.OPTION_PHONE_LANGUAGE)!!
    }
    fun setPreferredLocale(lang: String) {
        Log.i("TAG", "setPreferredLocale: $lang")
        val editor = preferences.edit()
        editor.putString("lang", lang)
        editor.apply()
    }


    companion object {
      /*  fun setPreferredLocale(context: Context, lang: String) {
            val sharedPreferences = context.getSharedPreferences("preferred_locale", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("lang", lang)
            editor.apply()
            Log.i("TAG", "saveSelectedLangToSharedPref: $lang")
        }

        fun getPreferredLocale(context: Context): String {
            Log.i("TAG", "getPreferredLocale: $context")
            val sharedPreferences =
                context.applicationContext
                    .getSharedPreferences(
                        "preferred_locale",
                        Context.MODE_PRIVATE,
                    )

            return sharedPreferences.getString("lang", "sys_def")!!
        }
*/
        // date
        fun getCurrentDate(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("date", Context.MODE_PRIVATE)
            return sharedPreferences.getString("date", "null")!!
        }

        fun setCurrent(context: Context, date: String) {

            val sharedPreferences = context.getSharedPreferences("date", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("date", date)
            editor.apply()

        }
        // wind unit
        fun getCurrentWindUnit(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("windUnit", Context.MODE_PRIVATE)
            return sharedPreferences.getString("windUnit", "m/s")!!
        }

        fun setWindUnit(context: Context, windUnit: String) {

            val sharedPreferences = context.getSharedPreferences("windUnit", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("windUnit", windUnit)
            editor.apply()

        }

        fun setLocation(context: Context, location: String) {
            if (getCurrentLocation(context) == location)
                return
            else {
                val sharedPreferences =
                    context.getSharedPreferences("location", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("location", location)
                editor.apply()
            }
        }

        fun getCurrentLocation(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("location", Context.MODE_PRIVATE)
            return sharedPreferences.getString("location", "gps")!!
        }

        // lang



// units

        fun getCurrentUnit(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("unit", Context.MODE_PRIVATE)
            //Log.i("TAG", "getCurrentMode: shared pref $mode")
            return sharedPreferences.getString("unit", "metric")!!
        }

        fun setUnit(context: Context, unit: String) {
            val sharedPreferences = context.getSharedPreferences("unit", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("unit", unit)
            editor.apply()
        }

        // theme
        fun setTheme(context: Context, mode: Int) {
            val sharedPreferences = context.getSharedPreferences("mode", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("mode", mode)
            editor.apply()

        }

        fun getCurrentTheme(context: Context): Int {
            val sharedPreferences = context.getSharedPreferences("mode", Context.MODE_PRIVATE)
            //Log.i("TAG", "getCurrentMode: shared pref $mode")
            return sharedPreferences.getInt("mode", 3)
        }

    }
}