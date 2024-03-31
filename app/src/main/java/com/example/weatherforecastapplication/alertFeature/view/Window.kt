package com.example.weatherforecastapplication.alertFeature.view

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alertFeature.model.DialogService


class Window(
    private val context: Context,
    private val description: String
) {
    private val view: View
    private var params: WindowManager.LayoutParams? = null
    private val windowManager: WindowManager
    private val layoutInflater: LayoutInflater

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }else{
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            );
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        view = layoutInflater.inflate(R.layout.popup_window_layout, null)
        view.findViewById<View>(R.id.dismiss).setOnClickListener { dismiss() }
      val descriptionTv =  view.findViewById<TextView>(R.id.dialog_desc)
        descriptionTv.text = description
        params!!.gravity = Gravity.TOP
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun open() {
        try {
            if (view.windowToken == null) {
                if (view.parent == null) {
                    windowManager.addView(view, params)
                }
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

    private fun dismiss() {
        try {
            context.stopService(Intent(context,DialogService::class.java))
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(view)
            view.invalidate()
            (view.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }
}