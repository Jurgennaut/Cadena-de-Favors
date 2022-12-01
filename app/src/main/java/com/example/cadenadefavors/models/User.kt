package com.example.cadenadefavors.models

data class User(val pUsername:String,val pPassword:String, val pEmail:String, val pPhone:String, val pAddress:String,){

    var Username:String?= null
    var Password:String?=null

    var Email:String?=null
    var Phone:String?=null
    var Address:String?=null
    var Offers: MutableList<Offer>?=null
    init {
        this.Username = pUsername
        this.Password= pPassword
        this.Email=pEmail
        this.Phone=pPhone
        this.Address=pAddress
        this.Offers=arrayListOf()
    }

}

