package com.example.cadenadefavors.models

data class Opinion(val owner:String, val description:String, val favorDone:String, val photo:String, val stars:Int) {

    var opinionOwner:String?=null
    var opinionDescription:String?=null
    var opinionerFavorReceived:String?=null
    var opinionerPhoto:String?=null
    var opinionerStarsGiven:Int?=null

    init {
        this.opinionOwner= owner
        this.opinionDescription = description
        this.opinionerFavorReceived=favorDone
        this.opinionerPhoto=photo
        this.opinionerStarsGiven=stars
    }
}