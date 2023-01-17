package org.bad_coder.weather.data.network

import org.bad_coder.weather.data.model.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeatherInfo(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") api_key: String,
        @Query("units") units: String = "imperial"
    ): WeatherDto
}