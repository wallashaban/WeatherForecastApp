package com.example.weatherforecastapplication.utils

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.SuperscriptSpan
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale


private const val TAG = "Constants"
const val BASE_URL: String = "https://api.openweathermap.org/"
const val API_KEY: String = "93d6a7e396a4d256d304951fb4f21c3a"
const val MAP_API_KEY: String = "AIzaSyBueNJuuCyZG5EmAYpfU2MYglQd-44YuT8"
var WIND_UNIT :String = "m/s"
const val REQUEST_LOCATION_CODE = 5005
const val REQUEST_PERMISSION_CODE = 5000



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
    return dateTime.toLocalDate().toString()
}

///////////////////////////////////////////////////////////////////////

// Themes functions
fun applyMode(context: Context) {
    if (Storage.getCurrentTheme(context) == 3)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
    else
        AppCompatDelegate.setDefaultNightMode(Storage.getCurrentTheme(context))
}

// check  location

fun requestPermission(activity: Activity) {
    Log.i(TAG, "requestLocation: ")
    requestPermissions(
        activity,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        REQUEST_LOCATION_CODE
    )
}

fun popMapFragmentFromTheBackStack(view: View){
    // pop map fragment from the back stack
    val navController=  Navigation.findNavController(view)
    navController.popBackStack(R.id.mapFragment,true)
}
fun enableLocationServices(activity: Activity) {
    Toast.makeText(activity, "Turn on location", Toast.LENGTH_SHORT).show()
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    activity.startActivity(intent)
}

fun isLocationEnable(context: Context): Boolean {
    val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE)
                as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

//////////////////////////////////
fun convertToMilePerHour(value:Double):Double{
    val speed = value * 2.23694
   return "%.2f"
       .format(Locale.US,speed)
       .toDouble()
}

fun convertToMeterPerSecond(value:Double):Double{
    val speed = value / 2.23694
    return "%.2f".format(Locale.US,speed).toDouble()
}

fun addCelsiusSign(temp:Double,context: Context):String{
    val unit = if (Storage.getCurrentUnit(context) == "metric")
        "C"
    else if (Storage.getCurrentUnit(context) == "standard")
        "F"
    else
        "K"
    val spannableString = SpannableString("${temp}\u00B0$unit")
    spannableString.setSpan(
        SuperscriptSpan(), temp.toString().length,
        spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString.toString()
}

////////////////////////

fun getAddressFromCoordinates(context: Context,
                              latitude: Double?, longitude: Double?): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    var addressText = ""
    try {
        val addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
        if (addresses != null && addresses.size > 0) {
            val address = addresses[0]
            for (i in 0..address.maxAddressLineIndex) {
                addressText += address.getAddressLine(i) + ", "
            }
            // Remove the trailing comma and space
            addressText = addressText.substring(0, addressText.length - 2)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return addressText
}

fun showDatePickerDialog(context: Context,date:TextView) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val minDate = calendar.timeInMillis


    val datePickerDialog = DatePickerDialog(
        context,
        { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth)
            date.text = selectedDate
            Toast.makeText(context,
                "Selected Date: $selectedDate",
                Toast.LENGTH_SHORT)
                .show()
        },
        year,
        month,
        day
    )
    datePickerDialog.datePicker.minDate = minDate;

    datePickerDialog.show()
}

fun showTimePickerDialog(context: Context,time:TextView) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        {
            view: TimePicker, hourOfDay: Int, minute: Int ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            time.text = selectedTime
            Toast.makeText(context, "Selected Time: $selectedTime", Toast.LENGTH_SHORT).show()
        },
        hour,
        minute,
        false
    )

    timePickerDialog.show()
}

fun checkOverlayPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (!Settings.canDrawOverlays(context)) {
            val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
           context.startActivity(myIntent)
        }
    }
}
//////////////////////////////////////////////////////////////////////
fun checkConnectivity(context: Context):Boolean{
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting()
}
/////////////////////////////////////////////////////////////////////
fun showSnackbar(activity: Activity,message: String) {
    val snackbar = Snackbar.make(
       activity.findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_SHORT
    )
    snackbar.show()
}
///////////////////////////////////////////////////////////////////
fun convertArabicDatetimeToEnglish(input: String): String {
    val arabicNumerals = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val englishNumerals = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    var result = input
    arabicNumerals.forEachIndexed { index, arabicNumeral ->
        result = result.replace(arabicNumeral, englishNumerals[index])
    }
    return result
}
fun showSnowOrRain(activity: Activity,value:Double){
    val temp = convertTempToCelsius(activity,value)
    Log.i(TAG, "showSnowOrRain: $temp")
    if(temp<=0) {
        activity
            .findViewById<LottieAnimationView>(R.id.snow)
            .visibility = View.VISIBLE
        activity
            .findViewById<LottieAnimationView>(R.id.rain)
            .visibility = View.GONE
    }else if(temp<=10)
    {
        activity
            .findViewById<LottieAnimationView>(R.id.rain)
            .visibility = View.VISIBLE
        activity
            .findViewById<LottieAnimationView>(R.id.snow)
            .visibility = View.GONE
    }else
    {
        activity
            .findViewById<LottieAnimationView>(R.id.rain)
            .visibility = View.GONE
        activity
            .findViewById<LottieAnimationView>(R.id.snow)
            .visibility = View.GONE
    }

}

fun convertTempToCelsius(context: Context,temp:Double):Double{
    if (Storage.getCurrentUnit(context)=="imperial")
        return kelvinToCelsius(temp)
    else  if (Storage.getCurrentUnit(context)=="standard")
        return fahrenheitToCelsius(temp)
    else
        return temp
}

fun convertTempToAproppiateUnit(){

}
fun kelvinToCelsius(kelvin: Double): Double {
    return kelvin - 273.15
}

fun celsiusToKelvin(celsius: Double): Double {
    return celsius + 273.15
}

fun fahrenheitToCelsius(fahrenheit: Double): Double {
    return (fahrenheit - 32)/ 1.79999999
}
fun celsiusToFahrenheit(celsius: Double): Double {
    return celsius * 9 / 5 + 32
}

fun isDark(context: Context): Boolean
{
   return Storage.getCurrentTheme(context) ==2
}

fun setCardViewBackground(context: Context):Int{
   return if(isDark(context))
        ContextCompat.getColor(context,R.color.light)
    else
        ContextCompat.getColor(context,R.color.light)
}


/*
fun translateToArabic(governorateName: String, callback: (String) -> Unit) {
    val options = FirebaseTranslatorOptions.Builder()
        .setSourceLanguage(FirebaseTranslateLanguage.EN)
        .setTargetLanguage(FirebaseTranslateLanguage.AR)
        .build()
    val englishArabicTranslator: FirebaseTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
    englishArabicTranslator.downloadModelIfNeeded()
        .addOnCompleteListener(object : OnCompleteListener<Void?> {
            override fun onComplete(task: Task<Void?>) {
                if (task.isSuccessful) {
                    englishArabicTranslator.translate(governorateName)
                        .addOnSuccessListener { translatedText ->
                            callback(translatedText)
                        }
                        .addOnFailureListener { exception ->
                            callback(governorateName)
                        }
                } else {
                    // Handle model download failure
                    callback(governorateName)
                }
            }
        })
}*/
