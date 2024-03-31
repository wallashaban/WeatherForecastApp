package com.example.weatherforecastapplication.alertFeature.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.alertFeature.model.MyWorkManager
import com.example.weatherforecastapplication.alertFeature.viewModel.AlertViewModel
import com.example.weatherforecastapplication.databinding.DialogLayoutBinding
import com.example.weatherforecastapplication.utils.convertArabicDatetimeToEnglish
import com.example.weatherforecastapplication.utils.getAddressFromCoordinates
import com.example.weatherforecastapplication.utils.isNotificationPermissionGranted
import com.example.weatherforecastapplication.utils.requestNotificationPermission
import com.example.weatherforecastapplication.utils.requestNotificationPermissions
import com.example.weatherforecastapplication.utils.showDatePickerDialog
import com.example.weatherforecastapplication.utils.showSnackbar
import com.example.weatherforecastapplication.utils.showTimePickerDialog
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit


class AlertDialog(context: Context,
                  val lat: Double,
                  val long: Double,
                  private val alertViewModel: AlertViewModel,
    val activity: Activity

                 ) : Dialog(context) {
                     private var isNotification :Boolean = true

    private lateinit var binding: DialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroupNotification.setOnCheckedChangeListener(object :
        RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

                val selectedRadioButton: RadioButton = findViewById(checkedId)
                        if( selectedRadioButton != findViewById(R.id.alarm))
                   {

                       if(isNotificationPermissionGranted(context))
                       {
                           isNotification = true
                       }else{
                           isNotification = true
                           requestNotificationPermissions(
                               activity
                           )
//                           Toast.makeText(context,
//                               "We don't have the permission please enable it ",
//                               Toast.LENGTH_SHORT)
//                               .show()
                       }
                   }else{
                       isNotification = false
                   }
            }

        })

        binding.dateAlert.setOnClickListener {
            showDatePickerDialog(context,binding.dateAlert)
        }

        binding.timeAlert.setOnClickListener {
           showTimePickerDialog(context,binding.timeAlert)
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.save.setOnClickListener {
            val datetime = binding.dateAlert.text.toString() +
                    " ${binding.timeAlert.text}"
            val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm",
                Locale("ar"))
            val ldt = LocalDateTime.parse(
                convertArabicDatetimeToEnglish(datetime), dtf)
            val millis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            scheduleWork(context,
                millis,
                lat,long,isNotification) // area of error take care
            dismiss()
        }
    }


    private fun scheduleWork(context: Context,targetTimeMillis:Long,lat:Double,long: Double,
                             isNotification:Boolean) {

        val datetime = "${binding. dateAlert.text} ${binding.timeAlert.text}"
        val workRequest = alertViewModel
            .scheduleWork(targetTimeMillis,lat,long,isNotification,datetime)

        WorkManager.getInstance(context).enqueue(workRequest)
        val alert = AlertRoom(
            workRequest.id,
            getAddressFromCoordinates(
                context,
                lat,
                long,
            ),
           datetime,
            lat,
            long,
        )
        alertViewModel.saveAlert(alert)
    }
}
