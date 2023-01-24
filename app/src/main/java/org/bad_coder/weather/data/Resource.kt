package org.bad_coder.weather.data

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(message: String = "Loading...", data: T? = null) : Resource<T>(data, message)
}
