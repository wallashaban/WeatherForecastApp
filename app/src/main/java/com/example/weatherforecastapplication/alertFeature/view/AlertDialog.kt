package com.example.weatherforecastapplication.alertFeature.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.RadioButton
import android.widget.Toast
import androidx.work.WorkManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.alertFeature.viewModel.AlertViewModel
import com.example.weatherforecastapplication.databinding.DialogLayoutBinding
import com.example.weatherforecastapplication.utils.checkOverlayPermission
import com.example.weatherforecastapplication.utils.convertArabicDatetimeToEnglish
import com.example.weatherforecastapplication.utils.getAddressFromCoordinates
import com.example.weatherforecastapplication.utils.showDatePickerDialog
import com.example.weatherforecastapplication.utils.showTimePickerDialog
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class AlertDialog(
    context: Context,
    private val lat: Double,
    private val long: Double,
    private val alertViewModel: AlertViewModel,
) : Dialog(context) {
    private var isNotification: Boolean = true

    private lateinit var binding: DialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroupNotification.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)
            isNotification = if (selectedRadioButton != findViewById(R.id.alarm)) {
                true
            } else {
                checkOverlayPermission(context)
                false
            }
        }

        binding.dateAlert.setOnClickListener {
            showDatePickerDialog(context, binding.dateAlert)
        }

        binding.timeAlert.setOnClickListener {
            showTimePickerDialog(context, binding.timeAlert)
        }

        binding.save.setOnClickListener {
            val date = binding.dateAlert.text.toString()
            val time = binding.timeAlert.text.toString()
            if (date.isNullOrEmpty() || time.isNullOrEmpty())
                Toast.makeText(context, "Please set date and time", Toast.LENGTH_SHORT)
                    .show()
            else {

                val millis = parseDatetime(date, time).atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()
                scheduleWork(
                    WorkParam(
                        context = context,
                        millis = millis,
                        isNotification = isNotification,
                        datetime = "$date $time"
                    )
                )
                dismiss()
            }
        }
    }

    private fun parseDatetime(date: String, time: String): LocalDateTime {
        val dtf = DateTimeFormatter.ofPattern(
            "yyyy/MM/dd HH:mm",
            Locale("ar")
        )
        val ldt = LocalDateTime.parse(
            convertArabicDatetimeToEnglish("$date $time"), dtf
        )
        return ldt
    }

    private fun scheduleWork(workParam: WorkParam) {
        val workRequest = alertViewModel
            .scheduleWork(workParam)

        WorkManager.getInstance(context).enqueue(workRequest)
        val alert = AlertRoom(
            workRequest.id,
            getAddressFromCoordinates(
                context,
                lat,
                long,
            ),
            workParam.datetime,
            lat,
            long,
        )
        alertViewModel.saveAlert(alert)
    }
}
