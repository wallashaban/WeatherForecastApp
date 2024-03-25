package com.example.weatherforecastapplication.alertFeature.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alertFeature.model.AlarmItem
import com.example.weatherforecastapplication.alertFeature.model.AlarmScheduler
import com.example.weatherforecastapplication.alertFeature.model.AlarmSchedulerImpl
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import com.example.weatherforecastapplication.alertFeature.model.MyWorkManager
import com.example.weatherforecastapplication.alertFeature.viewModel.AlertViewModel
import com.example.weatherforecastapplication.databinding.DialogLayoutBinding
import com.example.weatherforecastapplication.databinding.FragmentAlertBinding
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.shared.ApiState
import com.example.weatherforecastapplication.shared.checkOverlayPermission
import com.example.weatherforecastapplication.shared.convertDateToMillis
import com.example.weatherforecastapplication.shared.convertTimeToMillis
import com.example.weatherforecastapplication.shared.getAddressFromCoordinates
import com.example.weatherforecastapplication.shared.popMapFragmentFromTheBackStack
import com.example.weatherforecastapplication.shared.showDatePickerDialog
import com.example.weatherforecastapplication.shared.showTimePickerDialog
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit

private const val TAG = "AlertFragment"
class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertFactory: AlertViewModel.Factory
    private lateinit var adapter: AlertsAdapter
    private lateinit var manager : LinearLayoutManager
    private  var lat:Float = 0F
    private var long:Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertFactory = AlertViewModel.Factory(
            WeatherRepositoryImpl.getInstance(
                localDataSource = LocalDataSourceImpl.getInstance(requireContext()),
                remoteDataSource = RemoteDataSourceImpl
            )
        )
        alertViewModel = ViewModelProvider(
            requireActivity(),alertFactory
        )[AlertViewModel::class.java]


        lat = arguments?.getFloat("latitude") ?: 0F
        long = arguments?.getFloat("longitude") ?: 0F

        if(lat!=0F&&long!=0F) {
            checkOverlayPermission(requireContext())
            val context = requireContext()
            val alertDialog = AlertDialog(context,lat.toDouble(),long.toDouble(),
                alertViewModel)

            alertDialog.show()
        }
        alertViewModel.getAlert()
       }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentAlertBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popMapFragmentFromTheBackStack(requireView())
        binding.alertAdd.setOnClickListener {
            val action= AlertFragmentDirections
                .actionAlertFragmentToMapFragment("alert")
            Navigation.findNavController(requireView())
                .navigate(action)
        }


        adapter = AlertsAdapter(requireContext())
        {
            alert -> alertViewModel.deleteAlert(alert)
            WorkManager.getInstance(requireContext()).cancelWorkById(alert.id)
        }

        manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.alertRv.layoutManager = manager
        binding.alertRv.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                alertViewModel.alerts.collectLatest {result ->
                    when(result)
                    {
                        is ApiState.Loading->{
                            binding.alertRv.visibility = View.GONE
                            binding.progressBar2.visibility = View.VISIBLE
                        }
                        is ApiState.Failure ->{
                            binding.alertRv.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                result.error.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ApiState.Success ->{
                            binding.alertRv.visibility = View.VISIBLE
                            binding.progressBar2.visibility = View.GONE
                            adapter.submitList(result.data)
                        }
                    }
                }
            }
        }

    }

}