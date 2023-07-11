package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.MainActivity.Companion.API_KEY
import com.example.weatherapp.data_class.CurrentWeather
import com.example.weatherapp.data_class.HourlyWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class WeatherRepo(private val retrofitCall: RetrofitCall) {

    val currentWeather = MutableLiveData<CurrentWeather>()
    fun getCurrentWeather(lat:Double,lon:Double){
        val current = retrofitCall.getCurrentWeather(lat,lon,"metric",API_KEY)
        current.enqueue(object : Callback<CurrentWeather> {
            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                if(response.isSuccessful){
                    currentWeather.value = response.body()
                    println(response.body())
                    Log.i("Current Weather","SUCCESSFUL")
                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("Current Weather", t.message.toString())
            }
        })
    }

    val hourlyWeather = MutableLiveData<HourlyWeather>()
    fun getHourlyWeather(lat:Double,lon:Double){
        val hourly = retrofitCall.hourlyWeather(lat,lon,"metric",8,API_KEY)
        hourly.enqueue(object: Callback<HourlyWeather>{
            override fun onResponse(call: Call<HourlyWeather>, response: Response<HourlyWeather>) {
                if(response.isSuccessful){
                    hourlyWeather.value = response.body()
                    println(response.body())
                    Log.i("Hourly Weather","SUCCESSFUL")
                }
            }

            override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {
                Log.e("Hourly Weather",t.message.toString())
            }

        })
    }

    val weatherByCity = MutableLiveData<CurrentWeather>()
    fun getWeatherByCity(city:String){
        val mCity = retrofitCall.weatherByName(city,"metric", API_KEY)
        mCity.enqueue(object:Callback<CurrentWeather>{
            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                if(response.isSuccessful){
                    weatherByCity.value = response.body()
                    println(response.body())
                    Log.i("BY CITY","SUCC")
                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("BY CITY",t.message.toString())
            }

        })
    }

}