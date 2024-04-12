package com.example.weatherforecastapplication.utils

import android.content.Context

class SpinnerUtils {

    companion object {
        fun getTempSpinnerValue(context: Context): Int {
            return if (Storage.getCurrentUnit(context) == "metric")
                0
            else if (Storage.getCurrentUnit(context) == "standard")
                1
            else
                2
        }

        // Lang
        fun getLangSpinnerValue(context: Context): Int {

            return if (Storage(context).getPreferredLocale() == "en")
                0
            else if (Storage(context).getPreferredLocale() == "ar")
                1
            else
                2
        }


// location


        fun getCurrentSpinnerLocationValue(context: Context): Int {
            return if (Storage.getCurrentLocation(context) == "gps") {
                0
            } else
                1
        }

// wind Speed


        fun getWindUnitSpinnerValue(context: Context): Int {

            return if (Storage.getCurrentWindUnit(context) == "m/s")
                0
            else
                1
        }
    }
}