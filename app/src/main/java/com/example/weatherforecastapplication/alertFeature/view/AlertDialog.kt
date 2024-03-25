package com.example.weatherforecastapplication.alertFeature.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import com.example.weatherforecastapplication.alertFeature.model.MyWorkManager
import com.example.weatherforecastapplication.alertFeature.viewModel.AlertViewModel
import com.example.weatherforecastapplication.databinding.AlertLayoutBinding
import com.example.weatherforecastapplication.databinding.DialogLayoutBinding
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.shared.convertDateToMillis
import com.example.weatherforecastapplication.shared.convertTimeToMillis
import com.example.weatherforecastapplication.shared.getAddressFromCoordinates
import com.example.weatherforecastapplication.shared.showDatePickerDialog
import com.example.weatherforecastapplication.shared.showTimePickerDialog
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
import java.util.concurrent.TimeUnit

class AlertDialog(context: Context,
   val lat: Double,
   val long: Double,
    val alertViewModel: AlertViewModel
                 /* private val onSaveClickListener: () -> Unit*/) : Dialog(context) {

    private lateinit var binding: DialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            Log.i("TAG", "onCreate: ${convertDateToMillis(binding.dateAlert.text.toString())}")
            scheduleWork(context,
                convertDateToMillis(binding.dateAlert.text.toString())
                        + convertTimeToMillis(binding.timeAlert.text.toString()),
                lat,long)
            dismiss()
        }
    }

    private fun scheduleWork(context: Context,targetTimeMillis:Long,lat:Double,long: Double) {
        val inputData = Data.Builder()
            .putDouble("lat", lat)
            .putDouble("lon", long)
            .build()
        val currentTimeMillis = System.currentTimeMillis()
        val initialDelay = targetTimeMillis - currentTimeMillis
        Log.i("TAG", "scheduleWork: $initialDelay")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MyWorkManager>()
            .setConstraints(constraints)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
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
            long
        )
        alertViewModel.saveAlert(alert)
    }
}
