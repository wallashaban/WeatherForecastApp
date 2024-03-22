package com.example.weatherforecastapplication.alertFeature.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapplication.alertFeature.model.AlarmItem
import com.example.weatherforecastapplication.alertFeature.model.AlarmScheduler
import com.example.weatherforecastapplication.alertFeature.model.AlarmSchedulerImpl
import com.example.weatherforecastapplication.alertFeature.model.AlertRoom
import com.example.weatherforecastapplication.alertFeature.viewModel.AlertViewModel
import com.example.weatherforecastapplication.databinding.FragmentAlertBinding
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.shared.ApiState
import com.example.weatherforecastapplication.shared.getAddressFromCoordinates
import com.example.weatherforecastapplication.shared.popMapFragmentFromTheBackStack
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

private const val TAG = "AlertFragment"
class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertFactory: AlertViewModel.Factory
    private lateinit var adapter: AlertsAdapter
    private lateinit var manager : LinearLayoutManager
    private  var lat:Float = 0F
    private var long:Float = 0F
    private lateinit var scheduler: AlarmScheduler
    private lateinit var alarmItem: AlarmItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduler = AlarmSchedulerImpl(requireContext())

        alertFactory = AlertViewModel.Factory(
            WeatherRepositoryImpl.getInstance(
                localDataSource = LocalDataSourceImpl.getInstance(requireContext()),
                remoteDataSource = RemoteDataSourceImpl
            )
        )
        alertViewModel = ViewModelProvider(
            this,alertFactory
        )[AlertViewModel::class.java]


        lat = arguments?.getFloat("latitude") ?: 0F
        long = arguments?.getFloat("longitude") ?: 0F
      /* if(lat!=0F&&long!=0F)
       {
           val name = getAddressFromCoordinates(
               requireContext(),
               lat.toDouble(),
               long.toDouble(),
           )
           val alert = AlertRoom(
               name,"12:45 pm","2024/3/22",
               lat.toDouble(),
               long.toDouble())

           val alertDialog = AlertDialog(requireContext()) {

               Log.i(TAG, "onCreate: Saved")
           }
           alertDialog.show()
       }*/
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month, day, hour, minute+1)
        val alarmTimeMillis = calendar.timeInMillis
        alarmItem = AlarmItem(alarmTimeMillis,
            "oster ya rab")
        if(lat!=0F&&long!=0F) {
            val name = getAddressFromCoordinates(
                requireContext(),
                lat.toDouble(),
                long.toDouble(),
            )
            val alert = AlertRoom(
                name, "12:45 pm", "2024/3/22",
                lat.toDouble(),
                long.toDouble()
            )
            alertViewModel.saveAlert(alert)
            scheduler.scheduler(alarmItem)
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
            scheduler.cansel(alarmItem)
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