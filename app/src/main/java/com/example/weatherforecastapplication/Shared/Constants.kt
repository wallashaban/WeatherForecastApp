package com.example.weatherforecastapplication.Shared

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R

const val BASE_URL:String = "https://api.openweathermap.org/data/2.5/"
const val API_KEY:String = "93d6a7e396a4d256d304951fb4f21c3a"


@BindingAdapter("url")
fun loadImage(view: ImageView, url:String)
{
    // Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(view)

    Glide.with(view.context).load("https://openweathermap.org/img/wn/$url.png")
        .apply(
            RequestOptions().override(200, 200)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
        )
        .into(view)
}


// Themes functions
fun saveSelectedModeToSharedPref(context: Context,mode:Int)
{
    val sharedPreferences = context.getSharedPreferences("mode", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("mode", mode)
    editor.apply()
    changeMode(mode)
}

fun changeMode(mode:Int)
{
    when(mode) {
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

fun applyMode(context:Context)
{
    val mode = getCurrentMode(context)
    if(mode==3)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
    else
        AppCompatDelegate.setDefaultNightMode(mode)
}
fun getCurrentMode(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("mode", Context.MODE_PRIVATE)
    //Log.i("TAG", "getCurrentMode: shared pref $mode")
    return sharedPreferences.getInt("mode", 3)
}