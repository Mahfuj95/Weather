package org.bad_coder.weather.domain.model

data class WeatherInfo(
    var name: String = "",
    var drawable: String = "",
    var temperature: Double = 0.0,
    var description: String = "",
    var minTemperature: Double = 0.0,
    var maxTemperature: Double = 50.0
)