package org.bad_coder.weather.data.model

data class WeatherDto(
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
)

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)