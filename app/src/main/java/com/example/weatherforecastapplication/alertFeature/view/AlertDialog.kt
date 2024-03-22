package com.example.weatherforecastapplication.alertFeature.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.AlertLayoutBinding
import com.example.weatherforecastapplication.databinding.DialogLayoutBinding
import com.example.weatherforecastapplication.shared.showDatePickerDialog
import com.example.weatherforecastapplication.shared.showTimePickerDialog

class AlertDialog(context: Context,
                  private val onSaveClickListener: () -> Unit) : Dialog(context) {

    private lateinit var binding: DialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.date.setOnClickListener {
            showDatePickerDialog(context)
        }

        binding.time.setOnClickListener {
           showTimePickerDialog(context)
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.save.setOnClickListener {
            onSaveClickListener.invoke()
           // dismiss()
        }
    }
}
