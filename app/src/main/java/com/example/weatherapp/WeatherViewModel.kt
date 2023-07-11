package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data_class.City
import com.example.weatherapp.data_class.Coord
import com.example.weatherapp.data_class.CurrentWeather
import com.example.weatherapp.data_class.HourlyWeather

class WeatherViewModel(val weatherRepo: WeatherRepo,val currentLocation: CurrentLocation): ViewModel() {

    val currentWeather: LiveData<CurrentWeather> = weatherRepo.currentWeather
    fun getCurrentWeather(lat:Double ,lon:Double){
        weatherRepo.getCurrentWeather(lat,lon)
    }

    val location: LiveData<Coord> = currentLocation.coordinates
    fun getLocation(){
        currentLocation.getCurrentLocation()
    }

    val hourlyWeather: LiveData<HourlyWeather> = weatherRepo.hourlyWeather
    fun getHourlyWeather(lat: Double,lon: Double){
        weatherRepo.getHourlyWeather(lat,lon)
    }

    val weatherByCity:LiveData<CurrentWeather> = weatherRepo.weatherByCity
    fun getWeatherByCity(city: String){
        weatherRepo.getWeatherByCity(city)
    }
}