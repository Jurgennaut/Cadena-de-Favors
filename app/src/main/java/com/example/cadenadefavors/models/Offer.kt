package com.example.cadenadefavors.models

import android.os.Parcel
import android.os.Parcelable

data class Offer(val title: String?, val owner: String?, val category: String?, val price:Int, val description: String?, val image: String?): Parcelable{   // , val pUsers:ArrayList<User>){

    var offerTitle:String?
    var offerOwner:String?

    var offerCategory:String?
    var offerPrice:Int?
    var offerDescription:String?
    var offerImage:String?

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
        offerTitle = parcel.readString()
        offerOwner = parcel.readString()
        offerCategory = parcel.readString()
        offerPrice = parcel.readValue(Int::class.java.classLoader) as? Int
        offerDescription = parcel.readString()
        offerImage = parcel.readString()
    }
    //  var users: MutableList<User>?=null

    init {
        this.offerTitle = title
        this.offerOwner= owner
        this.offerCategory = category
        this.offerPrice=price
        this.offerDescription=description
        this.offerImage=image
        //     this.users=pUsers
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(owner)
        parcel.writeString(category)
        parcel.writeInt(price)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(offerTitle)
        parcel.writeString(offerOwner)
        parcel.writeString(offerCategory)
        parcel.writeValue(offerPrice)
        parcel.writeString(offerDescription)
        parcel.writeString(offerImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Offer> {
        override fun createFromParcel(parcel: Parcel): Offer {
            return Offer(parcel)
        }

        override fun newArray(size: Int): Array<Offer?> {
            return arrayOfNulls(size)
        }
    }

}

