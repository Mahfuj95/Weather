package org.bad_coder.weather.domain.usecase

import kotlinx.coroutines.flow.flow
import org.bad_coder.weather.data.Resource
import org.bad_coder.weather.data.network.weatherRepository.WeatherRepository
import org.bad_coder.weather.domain.model.CurrentLocation
import org.bad_coder.weather.domain.safeApiCall
import org.bad_coder.weather.domain.toWeatherInfo
import javax.inject.Inject

class GetCurrentWeatherInfo @Inject constructor() {
    suspend operator fun invoke(
        weatherSource: WeatherRepository,
        location: CurrentLocation
    ) = flow {
        emit(Resource.Loading("Loading weather information"))

        /**
         * Using safeApiCall to ensure the network call is dispatched to IO thread
         * and to handle any exception.
         **/
        emit(safeApiCall {
            weatherSource
                .getWeatherInfo(location)
                .toWeatherInfo()
        })
    }
}