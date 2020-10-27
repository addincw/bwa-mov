package com.addincendekia.bwa_mov.models

class FilmSeat {
    var seat: String ?= ""
    var status: Int ?= 0

    constructor(seat: String, status: Int){
        this.seat = seat
        this.status = status
    }
}