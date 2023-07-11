package com.example.weatherapp

import com.example.weatherapp.data_class.CurrentWeather
import com.example.weatherapp.data_class.HourlyWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitCall {

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") unit:String,
        @Query("appid") apiKey: String
    ): Call<CurrentWeather>

    @GET("forecast")
    fun hourlyWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") unit:String,
        @Query("cnt") count:Int,
        @Query("appid") apiKey: String
    ): Call<HourlyWeather>

    @GET("weather")
    fun weatherByName(
        @Query("q") city_name:String,
        @Query("units") units:String,
        @Query("appid") apiKey:String
    ): Call<CurrentWeather>
}