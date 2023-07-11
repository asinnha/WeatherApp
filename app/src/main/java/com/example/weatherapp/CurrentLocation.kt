package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data_class.Coord
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CurrentLocation(private val fusedLocation:FusedLocationProviderClient) {

    var coordinates = MutableLiveData<Coord>()

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){
        fusedLocation.lastLocation
            .addOnSuccessListener {
                coordinates.value = Coord(it.longitude,it.latitude)
                println("${it.longitude} ${it.latitude}")
                Log.i("Location","SUCCESSFUL")
            }
            .addOnFailureListener {
                Log.e("Location",it.message.toString())
            }
    }

}