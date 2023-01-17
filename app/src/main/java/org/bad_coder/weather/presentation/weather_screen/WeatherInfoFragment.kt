package org.bad_coder.weather.presentation.weather_screen

import android.view.View
import androidx.lifecycle.ViewModelProvider
import org.bad_coder.weather.R
import org.bad_coder.weather.databinding.FragmentWeatherInfoBinding
import org.bad_coder.weather.presentation.common.BaseFragment

class WeatherInfoFragment : BaseFragment<
        FragmentWeatherInfoBinding,
        WeatherInfoViewModel>(R.layout.fragment_weather_info) {

    override fun initView() {

    }

    override fun getBinding(view: View) = FragmentWeatherInfoBinding.bind(view)

    override fun createViewModel(): WeatherInfoViewModel {
        return ViewModelProvider(this)[WeatherInfoViewModel::class.java]
    }
}