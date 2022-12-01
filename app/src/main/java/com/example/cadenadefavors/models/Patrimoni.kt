package com.example.cadenadefavors.models

data class Patrimoni(val owner:String, val favos:Int) {

    var patrimoniOwner:String?=null
    var patrimoniFavos:Int?=null

    init {
        this.patrimoniOwner= owner
        this.patrimoniFavos=favos
    }
}