package org.bad_coder.weather.data.network.weatherRepository

import org.bad_coder.weather.data.model.WeatherDto
import org.bad_coder.weather.domain.model.CurrentLocation

interface WeatherRepository {
    suspend fun getWeatherInfo(curLoc: CurrentLocation): WeatherDto
}