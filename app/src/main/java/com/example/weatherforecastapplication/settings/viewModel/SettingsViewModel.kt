package com.example.weatherforecastapplication.settings.viewModel

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _unitsSharedFlow = MutableSharedFlow<Units>(replay = 1)
    val unitsSharedFlow = _unitsSharedFlow.asSharedFlow()
    private val _windSpeedSharedFlow = MutableSharedFlow<WindSpeed>(replay = 1)
    val windSpeedSharedFlow = _windSpeedSharedFlow.asSharedFlow()
    fun changeUnit(unit : Units){
        viewModelScope.launch {
            _unitsSharedFlow.emit(unit)
            Log.i("SettingsFragment", "changeUnit: ${unit.name}")
        }
    }
    fun changeWindSpeed(wind : WindSpeed){
        viewModelScope.launch {
            _windSpeedSharedFlow.emit(wind)
            Log.i("SettingsFragment", "changeUnit: ${wind.name}")
        }
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

}