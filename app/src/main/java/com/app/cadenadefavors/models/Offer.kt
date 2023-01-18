package com.app.cadenadefavors.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer(
    var Category:String ="",
    var Description:String="",
    var Image:String="",
    val Owner:String="",
    var Price:Int=0,
    var Title:String="",
    @DocumentId val documentId: String?=""):
    Parcelable

