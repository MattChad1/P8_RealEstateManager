package com.openclassrooms.realestatemanager.datas.model

import androidx.room.TypeConverter
import java.security.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.ofInstant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime.ofInstant
import java.util.*


class Converters {
    @TypeConverter
    fun getString(str: List<String?>?): String? {
        if (str == null) return null
        val toReturn = StringBuilder()
        for (s in str) toReturn.append(s).append(",")
        return toReturn.toString()
    }

    @TypeConverter
    fun setString(str: String?): List<String>? {
        return str?.split(",")
    }

    @TypeConverter
    fun getInt(ints: List<Int?>?): String? {
        if (ints == null) return null
        val toReturn = StringBuilder()
        for (i in ints) toReturn.append(i).append(",")
        return toReturn.toString()
    }

    @TypeConverter
    fun setInt(str: String?): List<Int?>? {
        val intNumbers : MutableList<Int?>? = mutableListOf<Int?>()
        val stringNumbers: List<String?>? = str?.split(",")
        if (stringNumbers == null) return null
        else {
            for (s in stringNumbers) intNumbers?.add(s?.toInt())
        }


                return intNumbers;
        }
    }

