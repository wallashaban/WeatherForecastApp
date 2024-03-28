package com.example.weatherforecastapplication.alertFeature.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
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
import com.example.weatherforecastapplication.utils.getAddressFromCoordinates
import com.example.weatherforecastapplication.utils.showDatePickerDialog
import com.example.weatherforecastapplication.utils.showTimePickerDialog
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class AlertDialog(context: Context,
                  val lat: Double,
                  val long: Double,
                  private val alertViewModel: AlertViewModel

                 ) : Dialog(context) {
                     private var isNotification = true

    private lateinit var binding: DialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroupNotification.setOnCheckedChangeListener(object :
        RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                Log.i("TAG", "onCheckedChanged: $checkedId")
                val selectedRadioButton: RadioButton = findViewById(checkedId)
                isNotification = selectedRadioButton != findViewById(R.id.alarm)
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
            Log.i("TAG", "onCreate: date" +binding.dateAlert.text.toString() +
                    " ${binding.timeAlert.text.toString()}")
            val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
            val ldt = LocalDateTime.parse(
                binding.dateAlert.text.toString() +
                    " ${binding.timeAlert.text}", dtf)
            val millis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            scheduleWork(context,
                millis,
                lat,long,isNotification)
            dismiss()
        }
    }

    private fun scheduleWork(context: Context,targetTimeMillis:Long,lat:Double,long: Double,
                             isNotification:Boolean) {
        val inputData = Data.Builder()
            .putDouble("lat", lat)
            .putDouble("lon", long)
            .putBoolean("notification",isNotification)
            .build()
        val currentTimeMillis = System.currentTimeMillis()
        val initialDelay = targetTimeMillis - currentTimeMillis
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MyWorkManager>()
            .setConstraints(constraints)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag("alert")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
        val alert = AlertRoom(
            workRequest.id,
            getAddressFromCoordinates(
                context,
                lat,
                long,
            ),
            binding.timeAlert.text.toString(),
           binding. dateAlert.text.toString(),
            lat,
            long,
        )
        alertViewModel.saveAlert(alert)
    }
}
