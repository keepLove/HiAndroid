package com.s.android.hiandroid.ui.android.aidl

import android.os.Parcel
import android.os.Parcelable

class Offer() : Parcelable {

    var id: Int = 0
    var name: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
    }

    constructor(id: Int, name: String) : this() {
        this.id = id
        this.name = name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Offer(id=$id, name=$name)"
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

