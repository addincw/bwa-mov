package com.addincendekia.bwa_mov.utils

import java.text.NumberFormat
import java.util.*

class Currency {
    private var value: Double

    constructor(value: Double?) {
        this.value = value!!
    }

    fun toRupiah(): String {
        val locale = Locale("in", "ID")
        return NumberFormat.getInstance(locale).format(value)
    }
}