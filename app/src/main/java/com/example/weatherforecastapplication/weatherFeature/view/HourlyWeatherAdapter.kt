package com.example.weatherforecastapplication.weatherFeature.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.HourlyWeatherLayoutBinding
import com.example.weatherforecastapplication.data.models.CurrentWeather

class HourlyWeatherAdapter(
    val context: Context,
    // var onProductClick: (CurrentWeather)->Unit,

) : ListAdapter<CurrentWeather, HourlyViewHolder>(ProductsDiffUtil()) {
    lateinit var binding: HourlyWeatherLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        binding = DataBindingUtil
            .inflate(
                LayoutInflater.from(context),
                R.layout.hourly_weather_layout, parent, false
            )
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val currentWeather: CurrentWeather = getItem(position)
        binding.weather = currentWeather
    }
}


class ProductsDiffUtil : DiffUtil.ItemCallback<CurrentWeather>() {
    override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem == newItem
    }

}


class HourlyViewHolder(private var layout: HourlyWeatherLayoutBinding) :
    RecyclerView.ViewHolder(layout.root)

