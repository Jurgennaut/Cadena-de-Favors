package com.example.cadenadefavors.models

data class Offer(val title:String, val owner:String, val category:String, val price:Int, val description:String, val image:String,){

    var offerTitle:String?= null
    var offerOwner:String?=null

    var offerCategory:String?=null
    var offerPrice:Int?=null
    var offerDescription:String?=null
    var offerImage:String?=null

    init {
        this.offerTitle = title
        this.offerOwner= owner
        this.offerCategory = category
        this.offerPrice=price
        this.offerDescription=description
        this.offerImage=image
    }

}

