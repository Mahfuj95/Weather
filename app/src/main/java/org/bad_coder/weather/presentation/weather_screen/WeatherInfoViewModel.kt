package org.bad_coder.weather.presentation.weather_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bad_coder.weather.data.Resource
import org.bad_coder.weather.data.network.weatherRepository.RemoteWeatherSource
import org.bad_coder.weather.domain.model.CurrentLocation
import org.bad_coder.weather.domain.model.WeatherInfo
import org.bad_coder.weather.domain.usecase.GetCurrentWeatherInfo
import org.bad_coder.weather.domain.usecase.GetFusedCurrentLocation
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject constructor(
    private val weatherSource: RemoteWeatherSource,
    private val getFusedCurrentLocation: GetFusedCurrentLocation,
    private val getCurrentWeatherInfo: GetCurrentWeatherInfo,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Nothing)
    val uiState: StateFlow<UiState> = _uiState

    fun onLocationPermissionGranted() {
        viewModelScope.launch {
            getFusedCurrentLocation().collect { result ->
                when (result) {
                    is Resource.Success -> getWeatherInformation(result.data!!)
                    is Resource.Error -> _uiState.emit(UiState.Error(result.message!!))
                    is Resource.Loading -> _uiState.emit(UiState.Loading(result.message!!))
                }
            }
        }
    }

    private suspend fun getWeatherInformation(location: CurrentLocation) {
        getCurrentWeatherInfo(weatherSource, location).collect { result ->
            _uiState.emit(
                when (result) {
                    is Resource.Success -> UiState.Success(result.data!!)
                    is Resource.Error -> UiState.Error(result.message)
                    is Resource.Loading -> UiState.Loading(result.message)
                }
            )
        }
    }

    sealed class UiState {
        data class Loading(val message: String?) : UiState()
        data class Success(val weatherInfo: WeatherInfo) : UiState()
        data class Error(val errorMsg: String?) : UiState()
        object Nothing : UiState()
    }
}

