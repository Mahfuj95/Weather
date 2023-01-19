package org.bad_coder.weather.domain

import org.bad_coder.weather.data.model.WeatherDto
import org.bad_coder.weather.domain.model.WeatherInfo

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        name = name,
        drawable = if (weather.isNotEmpty()) "_${weather[0].icon}" else "ic_broken_image",
        temperature = String.format("%.1f", main.temp).toDouble(),
        description = if (weather.isNotEmpty()) weather[0].main else "Unknown",
        minTemperature = main.temp_min,
        maxTemperature = main.temp_max,
    )
}

fun Double.fromKelvinToFahrenheit(): Double {
    return 1.8 * (this - 273) + 32
}

fun Double.fromKelvinToCelsius(): Double {
    return this - 273.15
}