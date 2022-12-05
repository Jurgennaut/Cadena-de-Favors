package com.example.cadenadefavors.models

data class Transaccio(val pPagador:String, val pCobrador:String, val pFavos:Int, val dataHora:Int) {

    var pagador:String?=null
    var cobrador:String?=null
    var favos:Int?=null
    var transaccioDataHora:Int?=null

    init {
        this.pagador= pPagador
        this.cobrador=pCobrador
        this.favos=pFavos
        this.transaccioDataHora=dataHora
    }
}