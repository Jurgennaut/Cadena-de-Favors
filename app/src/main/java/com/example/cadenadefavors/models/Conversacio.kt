package com.example.cadenadefavors.models

data class Conversacio(val pUser1:String, val pUser2:String, val pMissatges:ArrayList<Missatge>){

    var User1:String?= null
    var User2:String?= null
    var Missatges: MutableList<Missatge>?=null
    init {
        this.User1 = pUser1
        this.User2 = pUser2
        this.Missatges=pMissatges
    }

}

