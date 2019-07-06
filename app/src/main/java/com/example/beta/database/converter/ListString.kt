package com.example.beta.database.converter

import android.os.Parcel
import android.os.Parcelable

data class ListString(val list: List<String>):Parcelable {
    constructor(parcel: Parcel) : this(parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListString> {
        override fun createFromParcel(parcel: Parcel): ListString {
            return ListString(parcel)
        }

        override fun newArray(size: Int): Array<ListString?> {
            return arrayOfNulls(size)
        }
    }
}