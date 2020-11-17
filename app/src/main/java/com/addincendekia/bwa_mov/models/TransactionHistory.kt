package com.addincendekia.bwa_mov.models

class TransactionHistory {
    var title: String ?= ""
    var subtitle: String ?= ""
    var amount: Int ?= 0
    var type: Int ?= 1

    constructor(title: String, subtitle: String, amount: Int, type: Int){
        this.title = title
        this.subtitle = subtitle
        this.amount = amount
        this.type = type
    }
}