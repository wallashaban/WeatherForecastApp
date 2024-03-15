package com.example.weatherforecastapplication.shared

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


const val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
const val API_KEY: String = "93d6a7e396a4d256d304951fb4f21c3a"



@BindingAdapter("url")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context).load("https://openweathermap.org/img/wn/$url.png")
            .apply(
                RequestOptions().override(200, 200)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
            )
            .into(view)
    }

}


@BindingAdapter("time")
fun setTime(textView: TextView, dateTimeString: String?) {
    Log.i("TAG", "setTime: $dateTimeString")
    if (!dateTimeString.isNullOrEmpty()) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val timeString = dateTime.toLocalTime()
            .format(DateTimeFormatter.ofPattern("hh:mm a"))
        textView.text = timeString
        Log.i("TAG", "setTime: ${timeString}")
    }
}

@BindingAdapter("dayOfWeek")
fun setDayNameFromDateTime(textView: TextView, dateTimeString: String?) {
    if (!dateTimeString.isNullOrEmpty()) {
        textView.text = getDayOfTheWeek(dateTimeString)
    }
}

fun getDayOfTheWeek(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)
    return dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}

fun getDateFromDateTime(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)
    val dateString = dateTime.toLocalDate().toString()
    return dateString
}

// language

fun setLocalee(activity: Activity, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    Log.i("TAG", "setLocalee: ")
    val resources = activity.resources
    val configuration = Configuration(resources.configuration)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        configuration.setLocale(locale)
    } else {
        @Suppress("DEPRECATION")
        configuration.locale = locale
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        activity.createConfigurationContext(configuration)
    } else {
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    //recreate(activity)
}

fun setLocale(activity: Activity, languageCode: String?) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources = activity.resources
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun updateLocale(language: String, activity: Activity) {

    val locale = Locale(language)
    Log.i("TAG", "updateLocale: ")
    Locale.setDefault(locale)
    val config = Configuration()
    config.setLocale(locale)
    activity.resources.updateConfiguration(
        config,
        activity.resources.displayMetrics
    )
    //recreate(activity)
}


// Themes functions
fun saveSelectedModeToSharedPref(context: Context, mode: Int) {
    val sharedPreferences = context.getSharedPreferences("mode", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("mode", mode)
    editor.apply()
    changeMode(mode)
}



fun changeMode(mode: Int) {
    when (mode) {
        1 -> {
            Log.i("TAG", "changeMode: 1")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        2 -> {
            Log.i("TAG", "changeMode: 2")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        3 -> {
            Log.i("TAG", "changeMode: 3")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}

fun applyMode(context: Context) {
    val mode = getCurrentMode(context)
    if (mode == 3)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
    else
        AppCompatDelegate.setDefaultNightMode(mode)
}

fun getCurrentMode(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("mode", Context.MODE_PRIVATE)
    //Log.i("TAG", "getCurrentMode: shared pref $mode")
    return sharedPreferences.getInt("mode", 3)
}


// units

fun getCurrentUnit(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("unit", Context.MODE_PRIVATE)
    //Log.i("TAG", "getCurrentMode: shared pref $mode")
    return sharedPreferences.getString("unit", "metric")!!
}
fun getTempSpinnerValue(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("unit", Context.MODE_PRIVATE)
    //Log.i("TAG", "getCurrentMode: shared pref $mode")
    val temp = sharedPreferences.getString("unit", "metric")!!
    return if(temp == "metric")
        0
    else if (temp == "standard")
        1
    else
        2
}

fun saveSelectedUnitToSharedPref(context: Context, unit: String) {
    val sharedPreferences = context.getSharedPreferences("unit", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("unit", unit)
    editor.apply()
}