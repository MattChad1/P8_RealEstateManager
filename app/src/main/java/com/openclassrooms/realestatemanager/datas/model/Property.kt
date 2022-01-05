package com.openclassrooms.realestatemanager.datas.model

import java.time.LocalDate
import java.util.function.DoubleUnaryOperator

data class Property(
    var type: String?,
    var agent: String?,
    var price: Int?,
    var squareFeet: DoubleUnaryOperator?,
    var rooms: Int?,
    var description: String?,
    var photo: List<String>?,
    var adress: String?,
    var proximity: List<String>?,
    var status: String,
    var dateStartSell: LocalDate?,
    var dateSold: LocalDate? = null,
)