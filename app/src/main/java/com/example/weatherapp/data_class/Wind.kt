package com.example.weatherapp.data_class

import com.google.gson.annotations.SerializedName

data class Wind (

    @SerializedName("speed" ) var speed : Double? = null,
    @SerializedName("deg"   ) var deg   : Int?    = null

)