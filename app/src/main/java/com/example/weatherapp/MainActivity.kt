package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.weatherapp.data_class.CurrentWeather
import com.example.weatherapp.databinding.ActivityMainBinding
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    companion object{
        const val API_KEY = "6d03809d620350825663714d1366aa85"
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
    lateinit var binding: ActivityMainBinding
    val viewModel : WeatherViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
            return
        }

        if (!isGPSEnabled(this)) {
            AlertDialog.Builder(this)
                .setTitle("Please Turn:ON your GPS")
                .setPositiveButton("OK"){dialog,_ ->
                    promptToEnableGPS(this)
                    dialog.dismiss()
                }
                .setNegativeButton("CANCEL"){dialog,_->
                    dialog.dismiss()
                }
                .create()
                .show()
        }else{
            viewModel.getLocation()
        }

        binding.currentLocation.setOnClickListener {
            viewModel.getLocation()
        }

        viewModel.location.observe(this){
            it.lat?.let { lat ->
                it.lon?.let { lon ->
                    viewModel.getCurrentWeather(lat, lon)
                    viewModel.getHourlyWeather(lat,lon)
                }
            }
        }

        viewModel.currentWeather.observe(this){ it ->
           updateUI(it)
        }

        viewModel.hourlyWeather.observe(this){
            val rv = binding.hourlyRv
            rv.layoutManager = LinearLayoutManager(this)
            val adapter = RecyclerAdapter(it.list,this)
            rv.adapter = adapter
        }

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val searchCityWeather = binding.searchCity
        searchCityWeather.setOnEditorActionListener { view, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                viewModel.getWeatherByCity(view.text.toString())
                searchCityWeather.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(searchCityWeather.windowToken, 0)
                true
            }else{
                false
            }
        }

        viewModel.weatherByCity.observe(this){
            updateUI(it)
            it.coord?.lat?.let { lat -> it.coord?.lon?.let { lon ->
                viewModel.getHourlyWeather(lat, lon)
            } }
        }


    }

    private fun updateUI(it: CurrentWeather) {
        binding.cityName.text = it.name
        binding.temp.text = it.main?.temp.toString()
        binding.feelsLike.text = "feels like ${ it.main?.feelsLike.toString()}°C"
        binding.wind.text = it.wind?.speed.toString() + " km/h  " + it.wind?.deg+"°"
        binding.humidity.text = it.main?.humidity.toString()+"%"
        it.weather.forEach {weather->
            weather.icon?.let { icon ->
                Glide.with(this@MainActivity)
                    .load("https://openweathermap.org/img/wn/${icon}@2x.png")
                    .into(binding.weatherIcon)
            }
            binding.weatherDesc.text = weather.description
            binding.weatherTitle.text = weather.main
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val currentDate = dateFormat.format(calendar.time)
        val currentTime = timeFormat.format(calendar.time)
        binding.dateTime.text = currentDate+" "+currentTime

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                viewModel.getLocation()
            }else{
                ToastFactory().toast(this,"Permission are denied")
            }
        }
    }

    private fun isGPSEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun promptToEnableGPS(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}