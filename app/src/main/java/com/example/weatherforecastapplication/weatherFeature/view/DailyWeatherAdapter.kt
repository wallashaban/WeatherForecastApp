package com.example.weatherforecastapplication.weatherFeature.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.DailyWeatherLayoutBinding
import com.example.weatherforecastapplication.data.models.CurrentWeather
import com.example.weatherforecastapplication.utils.setCardViewBackground

class DailyWeatherAdapter(
    private val context: Context,
    // var onProductClick: (CurrentWeather)->Unit,

) : ListAdapter<CurrentWeather, ViewHolder>(WeatherDiffUtil()) {
    lateinit var binding: DailyWeatherLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil
            .inflate(
                LayoutInflater.from(context),
                R.layout.daily_weather_layout, parent, false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.dailyCard.setCardBackgroundColor(setCardViewBackground(context))
        val currentWeather: CurrentWeather = getItem(position)
        binding.weather = currentWeather
    }
}


class WeatherDiffUtil : DiffUtil.ItemCallback<CurrentWeather>() {
    override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem == newItem
    }

}


class ViewHolder(var layout: DailyWeatherLayoutBinding) : RecyclerView.ViewHolder(layout.root)

