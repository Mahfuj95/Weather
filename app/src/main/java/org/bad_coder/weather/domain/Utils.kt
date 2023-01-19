package org.bad_coder.weather.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bad_coder.weather.data.Resource
import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            Resource.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    Resource.Error("Couldn't reach server. Please check your internet connection")
                }
                is HttpException -> {
                    Resource.Error("HttpException: Unknown response ${throwable.code()}")
                }
                else -> {
                    throw throwable
                }
            }
        }
    }
}

