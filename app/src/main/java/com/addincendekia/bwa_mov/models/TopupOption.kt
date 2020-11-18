package com.addincendekia.bwa_mov.models

class TopupOption {
    var option: Int ?= 0
    var selected: Boolean ?= false

    constructor(option: Int, selected: Boolean){
        this.option = option
        this.selected = selected
    }
}