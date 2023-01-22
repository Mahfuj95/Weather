package org.bad_coder.weather.presentation.weather_screen

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import org.bad_coder.weather.R
import org.bad_coder.weather.databinding.FragmentWeatherInfoBinding
import org.bad_coder.weather.domain.model.WeatherInfo
import org.bad_coder.weather.presentation.common.BaseFragment
import org.bad_coder.weather.presentation.common.getDrawableByName
import org.bad_coder.weather.presentation.common.toHide
import org.bad_coder.weather.presentation.common.toShow
import org.bad_coder.weather.presentation.weather_screen.WeatherInfoViewModel.UiState
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class WeatherInfoFragment : BaseFragment<
        FragmentWeatherInfoBinding,
        WeatherInfoViewModel>(R.layout.fragment_weather_info), EasyPermissions.PermissionCallbacks {
    companion object {
        const val PERMISSION_REQUEST_CODE = 111
    }

    override fun initView() {
        listenUiStateAndRenderView()
        requestPermission()
    }

    override fun getBinding(view: View) = FragmentWeatherInfoBinding.bind(view)

    override fun createViewModel(): WeatherInfoViewModel {
        return ViewModelProvider(this)[WeatherInfoViewModel::class.java]
    }

    /**
     * Listen to view model and update the ui accordingly
     **/
    private fun listenUiStateAndRenderView() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding.loadingView.toShow()
                        binding.loadingStatus.text = uiState.message
                    }
                    is UiState.Success -> {
                        populateWeatherInformation(uiState.weatherInfo)
                    }
                    is UiState.Error -> {
                        binding.errorMsg.text = uiState.errorMsg
                        binding.loadingView.toHide()
                        binding.errorView.toShow()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun populateWeatherInformation(weatherInfo: WeatherInfo) {
        binding.apply {
            curTemperature.text = weatherInfo.temperature.toString()
            temSymbol.text = "℉"
            locationName.text = weatherInfo.name
            weatherDescription.text = weatherInfo.description
            minTemperature.text = weatherInfo.minTemperature.toString()
            maxTemperature.text = weatherInfo.maxTemperature.toString()

            // Hide the loading view
            loadingView.toHide()
            weatherView.toShow()
            weatherVisualIcon.setImageResource(
                getDrawableByName(weatherInfo.drawable)
            )
        }
    }

    private fun requestPermission() {
        val perms = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
            viewModel.onLocationPermissionGranted()
            return
        }

        EasyPermissions.requestPermissions(
            this,
            "We need to location permission to know your location",
            PERMISSION_REQUEST_CODE,
            *perms
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        viewModel.onLocationPermissionGranted()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }
}