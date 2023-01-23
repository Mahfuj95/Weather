package org.bad_coder.weather.presentation.weather_screen

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.bad_coder.weather.data.Resource
import org.bad_coder.weather.data.network.weatherRepository.RemoteWeatherSource
import org.bad_coder.weather.domain.model.CurrentLocation
import org.bad_coder.weather.domain.model.WeatherInfo
import org.bad_coder.weather.domain.usecase.GetCurrentWeatherInfo
import org.bad_coder.weather.domain.usecase.GetFusedCurrentLocation
import org.bad_coder.weather.presentation.weather_screen.WeatherInfoViewModel.UiState
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherInfoViewModelTest {
    @MockK
    private val currentLocation: CurrentLocation = mockk()

    @MockK
    private val weatherInfo: WeatherInfo = mockk()

    @MockK
    private val getFusedCurrentLocation: GetFusedCurrentLocation = mockk()

    @MockK
    private val getCurrentWeatherInfo: GetCurrentWeatherInfo = mockk()

    @MockK
    private val weatherSource: RemoteWeatherSource = mockk()

    private lateinit var viewModel: WeatherInfoViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        viewModel = WeatherInfoViewModel(
            weatherSource,
            getFusedCurrentLocation,
            getCurrentWeatherInfo
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onLocationPermissionGranted should update the uiState to Loading when getCurrentLocation is Loading`() =
        runBlocking {
            every { getFusedCurrentLocation() } returns
                    flow { emit(Resource.Loading()) }

            viewModel.onLocationPermissionGranted()
            dispatcher.scheduler.advanceUntilIdle()
            assertThat(viewModel.uiState.value).isInstanceOf(Resource.Success::class.java)
        }

    @Test
    fun `onLocationPermissionGranted should retrieve weather information`() = runBlocking {
        coEvery { getFusedCurrentLocation() } returns
                flow { emit(Resource.Success(currentLocation)) }
        coEvery { getCurrentWeatherInfo(weatherSource, currentLocation) } returns
                flow { emit(Resource.Success(weatherInfo)) }

        viewModel.onLocationPermissionGranted()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(UiState.Success::class.java)
    }

    @Test
    fun `onLocationPermissionGranted should update isLoading when loading`() {
        coEvery { getFusedCurrentLocation() } returns
                flow { emit(Resource.Success(currentLocation)) }
        coEvery { getCurrentWeatherInfo(weatherSource, currentLocation) } returns
                flow { emit(Resource.Loading()) }

        viewModel.onLocationPermissionGranted()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(UiState.Loading::class.java)
    }

    @Test
    fun `onLocationPermissionGranted should update isError when error`() = runBlocking {
        coEvery { getFusedCurrentLocation() } returns
                flow { emit(Resource.Error("currentLocation")) }
        coEvery { getCurrentWeatherInfo(weatherSource, currentLocation) } returns
                flow { emit(Resource.Success(weatherInfo)) }

        viewModel.onLocationPermissionGranted()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value).isInstanceOf(UiState.Error::class.java)
    }
}