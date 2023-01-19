package org.bad_coder.weather.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.bad_coder.weather.domain.usecase.GetFusedCurrentLocation

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModules {
    @Provides
    fun provideGetFusedCurrentLocationUseCase(application: Application): GetFusedCurrentLocation {
        return GetFusedCurrentLocation(application)
    }
}