package com.example.weatherforecastapplication.alertFeature.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.databinding.AlertLayoutBinding
import com.example.weatherforecastapplication.utils.setCardViewBackground

class AlertsAdapter(
    val context: Context,
    private val listener: (alert: AlertRoom)->Unit,
) : ListAdapter<AlertRoom, AlertViewHolder>(
    AlertsDiffUtil()
) {

    private lateinit var binding: AlertLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        binding = DataBindingUtil
            .inflate(
                LayoutInflater.from(context),
                R.layout.alert_layout, parent, false
            )
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        binding.alertCard.setCardBackgroundColor( setCardViewBackground(context))

        val current = getItem(position)
        binding.alert = current
        binding.deleteAlert.setOnClickListener {
            listener.invoke(current)
        }

    }
}

class AlertViewHolder(val layout: AlertLayoutBinding) : RecyclerView.ViewHolder(layout.root)
class AlertsDiffUtil : DiffUtil.ItemCallback<AlertRoom>() {
    override fun areItemsTheSame(oldItem: AlertRoom, newItem: AlertRoom): Boolean {
        return oldItem.latitude == newItem.latitude
    }

    override fun areContentsTheSame(oldItem: AlertRoom, newItem: AlertRoom): Boolean {
        return oldItem == newItem
    }

}