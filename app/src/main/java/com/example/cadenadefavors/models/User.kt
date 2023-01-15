package com.example.cadenadefavors.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var Active: Boolean=true,
    var Password:String="",
    val Email:String="",
    var Username:String="",
    var Phone:String="",
    var Photo:String="",
    var Postalcode:String="",
    var Puntuation:Float=0.toFloat())
    : Parcelable {


}

