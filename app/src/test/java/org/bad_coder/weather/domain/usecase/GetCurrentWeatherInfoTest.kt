package org.bad_coder.weather.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.bad_coder.weather.data.Resource
import org.bad_coder.weather.data.model.WeatherDto
import org.bad_coder.weather.data.network.weatherRepository.RemoteWeatherSource
import org.bad_coder.weather.domain.model.CurrentLocation
import org.bad_coder.weather.domain.model.WeatherInfo
import org.bad_coder.weather.domain.toWeatherInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentWeatherInfoTest {
    @MockK private val weatherSource: RemoteWeatherSource = mockk()
    @MockK private val currentLocation: CurrentLocation = mockk()
    @MockK private val weatherInfo: WeatherInfo = mockk()
    @MockK private val weatherDto: WeatherDto = mockk()

    private lateinit var getCurrentWeatherInfo: GetCurrentWeatherInfo
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mockkStatic("org.bad_coder.weather.domain.ConvertersKt")
        Dispatchers.setMain(dispatcher)
        getCurrentWeatherInfo = GetCurrentWeatherInfo()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GetCurrentWeatherInfo should return a flow`() = runBlocking {
        coEvery { weatherSource.getWeatherInfo(currentLocation).toWeatherInfo() } returns weatherInfo
        val res = getCurrentWeatherInfo(weatherSource, currentLocation)
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(res).isInstanceOf(Flow::class.java)
    }

    @Test
    fun `GetCurrentWeatherInfo should emit Loading on invoke`() = runBlocking {
        coEvery { weatherSource.getWeatherInfo(currentLocation).toWeatherInfo() } returns weatherInfo

        val res = getCurrentWeatherInfo(weatherSource, currentLocation)
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(res.first()).isInstanceOf(Resource.Loading::class.java)
    }
    
    @Test
    fun `GetCurrentWeatherInfo should emit Error when exception occurs in network call`() = runBlocking {
        coEvery { weatherSource.getWeatherInfo(currentLocation) } throws IOException("Test IO Exception")

        val res = getCurrentWeatherInfo(weatherSource, currentLocation)
        dispatcher.scheduler.advanceUntilIdle()
        
        val response = res.toList()
        assertThat(response.count()).isEqualTo(2)
        assertThat(response[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(response[1]).isInstanceOf(Resource.Error::class.java)
    }
    
    @Test
    fun `GetCurrentWeatherInfo should emit Success when network call is successful`() = runBlocking {
        coEvery { weatherSource.getWeatherInfo(currentLocation) } returns weatherDto
        coEvery { weatherDto.toWeatherInfo() } returns weatherInfo

        val res = getCurrentWeatherInfo(weatherSource, currentLocation)
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(res.last()).isInstanceOf(Resource.Success::class.java)
    }
}