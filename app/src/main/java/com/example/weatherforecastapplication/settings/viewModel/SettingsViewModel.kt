package com.example.weatherforecastapplication.settings.viewModel

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.data.models.Language
import com.example.weatherforecastapplication.data.models.Units
import com.example.weatherforecastapplication.data.models.WindSpeed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _unitsSharedFlow = MutableSharedFlow<Units>(replay = 1)
    val unitsSharedFlow = _unitsSharedFlow.asSharedFlow()
    private val _windSpeedSharedFlow = MutableSharedFlow<WindSpeed>(replay = 1)
    val windSpeedSharedFlow = _windSpeedSharedFlow.asSharedFlow()

    private val _langSharedFlow = MutableSharedFlow<Language>(replay = 1)
    val langSharedFlow = _langSharedFlow.asSharedFlow()
    fun changeUnit(unit : Units){
        viewModelScope.launch {
            _unitsSharedFlow.emit(unit)
        }
    }
    fun changeWindSpeed(wind : WindSpeed){
        viewModelScope.launch {
            _windSpeedSharedFlow.emit(wind)
        }
    }

    fun changeLanguage(language: Language ){
        viewModelScope.launch {
            _langSharedFlow.emit(language)
            Log.i("TAG", "changeLanguage: $language")
        }
    }

    fun changeMode(mode: Int) {
        when (mode) {
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            3 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

}