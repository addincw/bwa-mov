package com.addincendekia.bwa_mov.models

import android.os.Parcel
import android.os.Parcelable

class Film() : Parcelable {
    var desc: String ?= ""
    var director: String ?= ""
    var genre: String ?= ""
    var judul: String ?= ""
    var poster: String ?= ""
    var rating: String ?= ""

    constructor(parcel: Parcel) : this() {
        desc = parcel.readString()
        director = parcel.readString()
        genre = parcel.readString()
        judul = parcel.readString()
        poster = parcel.readString()
        rating = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(desc)
        parcel.writeString(director)
        parcel.writeString(genre)
        parcel.writeString(judul)
        parcel.writeString(poster)
        parcel.writeString(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Film> {
        override fun createFromParcel(parcel: Parcel): Film {
            return Film(parcel)
        }

        override fun newArray(size: Int): Array<Film?> {
            return arrayOfNulls(size)
        }
    }
}
