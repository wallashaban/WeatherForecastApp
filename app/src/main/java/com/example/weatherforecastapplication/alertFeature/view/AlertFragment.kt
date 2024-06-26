package com.example.weatherforecastapplication.alertFeature.view

import android.content.DialogInterface
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
import androidx.work.WorkManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alertFeature.viewModel.AlertViewModel
import com.example.weatherforecastapplication.databinding.FragmentAlertBinding
import com.example.weatherforecastapplication.data.local.LocalDataSourceImpl
import com.example.weatherforecastapplication.data.models.AlertRoom
import com.example.weatherforecastapplication.data.models.Daos
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.remote.RemoteDataSourceImpl
import com.example.weatherforecastapplication.utils.ApiState
import com.example.weatherforecastapplication.utils.checkConnectivity
import com.example.weatherforecastapplication.utils.checkOverlayPermission
import com.example.weatherforecastapplication.utils.popMapFragmentFromTheBackStack
import com.example.weatherforecastapplication.utils.showSnackbar
import com.example.weatherforecastapplication.data.repo.WeatherRepositoryImpl
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AlertFragment"

class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertFactory: AlertViewModel.Factory
    private lateinit var adapter: AlertsAdapter
    private lateinit var manager: LinearLayoutManager
    private var lat: Float = 0F
    private var long: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertFactory = AlertViewModel.Factory(
            WeatherRepositoryImpl.getInstance(
                localDataSource = LocalDataSourceImpl.getInstance(Daos(requireContext())),
                remoteDataSource = RemoteDataSourceImpl
            )
        )
        alertViewModel = ViewModelProvider(
            requireActivity(), alertFactory
        )[AlertViewModel::class.java]


        lat = arguments?.getFloat("latitude") ?: 0F
        long = arguments?.getFloat("longitude") ?: 0F

        if (lat != 0F && long != 0F) {
            checkOverlayPermission(requireContext())
            val context = requireContext()
            val alertDialog = AlertDialog(
                context, lat.toDouble(), long.toDouble(),
                alertViewModel,
            )

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
            if (checkConnectivity(requireContext())) {
                val action = AlertFragmentDirections
                    .actionAlertFragmentToMapFragment("alert")
                Navigation.findNavController(requireView())
                    .navigate(action)
            } else {
                showSnackbar(requireActivity(), getString(R.string.noInternetMessage))
            }
        }


        adapter = AlertsAdapter(requireContext())
        { alert ->
            showAlertDialog(alert)
        }

        manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.alertRv.layoutManager = manager
        binding.alertRv.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                alertViewModel.alerts.collectLatest { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            binding.alertRv.visibility = View.GONE
                            binding.progressBar2.visibility = View.VISIBLE
                        }

                        is ApiState.Failure -> {
                            binding.alertRv.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                result.error.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ApiState.Success -> {
                            if (result.data.size == 0) {
                                binding.noAlert.visibility = View.VISIBLE
                            } else {
                                binding.noAlert.visibility = View.GONE
                            }
                            binding.alertRv.visibility = View.VISIBLE
                            binding.progressBar2.visibility = View.GONE
                            adapter.submitList(result.data)
                        }
                    }
                }
            }

        }

    }

    private fun showAlertDialog(alert: AlertRoom) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.alertMessage))
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog: DialogInterface?, which: Int ->
                alertViewModel.deleteAlert(alert)
                WorkManager.getInstance(requireContext()).cancelWorkById(alert.id)
            }
            .setNegativeButton(
                getString(R.string.no)
            ) { dialog: DialogInterface?, which: Int -> }
            .show()
    }
}