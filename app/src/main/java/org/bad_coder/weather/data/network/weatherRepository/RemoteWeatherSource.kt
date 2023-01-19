package org.bad_coder.weather.data.network.weatherRepository

import org.bad_coder.weather.BuildConfig
import org.bad_coder.weather.data.model.WeatherDto
import org.bad_coder.weather.data.network.WeatherApi
import org.bad_coder.weather.domain.model.CurrentLocation
import javax.inject.Inject

class RemoteWeatherSource @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {
    override suspend fun getWeatherInfo(curLoc: CurrentLocation): WeatherDto =
        weatherApi.getWeatherInfo(
            curLoc.lat, curLoc.lon, BuildConfig.API_KEY
        )
}