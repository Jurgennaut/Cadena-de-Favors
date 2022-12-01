package com.example.cadenadefavors.models

data class Missatge(val owner:String, val description:String, val dataHora:Int) {

    var missatgeOwner:String?=null
    var missatgeDescription:String?=null
    var missatgeDataHora:Int?=null

    init {
        this.missatgeOwner= owner
        this.missatgeDescription = description
        this.missatgeDataHora=dataHora
    }
}