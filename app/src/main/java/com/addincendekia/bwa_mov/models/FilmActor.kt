package com.addincendekia.bwa_mov.models

import android.os.Parcel
import android.os.Parcelable

class FilmActor() : Parcelable {
    var nama: String ?= ""
    var url: String ?= ""

    constructor(parcel: Parcel) : this() {
        nama = parcel.readString()
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilmActor> {
        override fun createFromParcel(parcel: Parcel): FilmActor {
            return FilmActor(parcel)
        }

        override fun newArray(size: Int): Array<FilmActor?> {
            return arrayOfNulls(size)
        }
    }
}