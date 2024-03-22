package com.example.weatherforecastapplication.shared

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
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
import androidx.core.app.ActivityCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

private const val TAG = "Constants"
const val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
const val API_KEY: String = "93d6a7e396a4d256d304951fb4f21c3a"
const val MAP_API_KEY: String = "AIzaSyBueNJuuCyZG5EmAYpfU2MYglQd-44YuT8"
var WIND_UNIT :String = "m/s"
const val REQUEST_LOCATION_CODE = 5005



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


fun getTempSpinnerValue(context: Context): Int {
    return if(Storage.getCurrentUnit(context) == "metric")
        0
    else if (Storage.getCurrentUnit(context) == "standard")
        1
    else
        2
}
// Lang
fun getLangSpinnerValue(context: Context): Int {

    return if(Storage.getPreferredLocale(context) == "en")
        0
    else if (Storage.getPreferredLocale(context) == "ar")
        1
    else
        2
}


// location


fun getCurrentSpinnerLocationValue(context: Context): Int {
    return if (Storage.getCurrentLocation(context)=="gps")
    {
        0
    }else
        1
}

// wind Speed


fun getWindUnitSpinnerValue(context: Context): Int {

    return if(Storage.getCurrentWindUnit(context)=="m/s")
        0
    else
        1
}




// check  location

fun requestLocation(activity: Activity) {
    Log.i(TAG, "requestLocation: ")
    ActivityCompat.requestPermissions(
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
   return "%.2f".format(speed).toDouble()
}

fun convertToMeterPerSecond(value:Double):Double{
    val speed = value / 2.23694
    return "%.2f".format(speed).toDouble()
}

fun addCelsiusSign(temp:Double,context: Context):String{
    val unit = if (Storage.getCurrentUnit(context) == "metric")
        "C"
    else
        ""
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



fun showDatePickerDialog(context: Context) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val minDate = calendar.timeInMillis


    val datePickerDialog = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            Toast.makeText(context, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
        },
        year,
        month,
        day
    )
    datePickerDialog.datePicker.minDate = minDate;

    datePickerDialog.show()
}

fun showTimePickerDialog(context: Context) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        TimePickerDialog.OnTimeSetListener {
            view: TimePicker, hourOfDay: Int, minute: Int ->
            val selectedTime = "$hourOfDay:$minute"
            Toast.makeText(context, "Selected Time: $selectedTime", Toast.LENGTH_SHORT).show()
        },
        hour,
        minute,
        false
    )

    timePickerDialog.show()
}