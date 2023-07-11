package com.example.weatherapp

import com.example.weatherapp.MainActivity.Companion.BASE_URL
import com.example.weatherapp.data_class.CurrentWeather
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module{

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitCall::class.java)
    }

    single {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single {
        CurrentLocation(get())
    }

    single{
        WeatherRepo(get())
    }
    viewModel {
        WeatherViewModel(get(),get())
    }
}