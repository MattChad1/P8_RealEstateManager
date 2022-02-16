package com.openclassrooms.realestatemanager.datas.model

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Converters {

//    @TypeConverter
//    fun getString(date: Date): String {
//        return date.toString()
//    }
//
//    @TypeConverter
//    fun setString(str: String): Date {
//        return LocalDate.parse(str, DateTimeFormatter.ISO_DATE)
//    }

//    @TypeConverter
//    fun getString(str: List<String?>?): String? {
//        if (str == null) return null
//        val toReturn = StringBuilder()
//        for (s in str) toReturn.append(s).append(",")
//        return toReturn.toString()
//    }
//
//    @TypeConverter
//    fun setString(str: String?): List<String>? {
//        return str?.split(",")
//    }
//
//    @TypeConverter
//    fun getInt(ints: List<Int?>?): String? {
//        if (ints == null) return null
//        val toReturn = StringBuilder()
//        for (i in ints) toReturn.append(i).append(",")
//        return toReturn.toString()
//    }
//
//    @TypeConverter
//    fun setInt(str: String?): List<Int?>? {
//        val intNumbers: MutableList<Int?>? = mutableListOf<Int?>()
//        val stringNumbers: List<String?>? = str?.split(",")
//        if (stringNumbers == null) return null
//        else {
//            for (s in stringNumbers) intNumbers?.add(s?.toInt())
//        }
//
//
//        return intNumbers;
//    }

//    @TypeConverter
//    fun getStringFromType(t: TypeEnum?): String? {
//        if (t == null) return null
//        else return t.str
//    }
//
//    @TypeConverter
//    fun getTypeFromString(s: String?): TypeEnum? {
//        for (x in TypeEnum.values()) {
//            if (x.str == s) return x
//        }
//        return null
//    }

//    @TypeConverter
//    fun getStringFromProximity(list: List<ProximityEnum>?): String? {
//        if (list == null) return null
//        else {
//            val st = StringBuilder()
//            for (p in list) {
//                if (st.isNotEmpty()) st.append(",")
//                st.append(p.str)
//            }
//            return st.toString()
//        }
//    }
//
//    @TypeConverter
//    fun getProximityFromString(s: String?): List<ProximityEnum?>? {
//        val prox = mutableListOf<ProximityEnum?>()
//        val arr = s?.split(",")
//        if (arr != null) {
//            for (value in ProximityEnum.values()) {
//                if (arr.contains(value.str)) prox.add(value)
//            }
//            return prox
//        }
//        return null
//    }

//    @TypeConverter
//    fun getStringFromPhotos(list: List<ImageRoom>?): String? {
//        if (list == null) return null
//        else return Json.encodeToString(list)
//
//    }
//
//    @TypeConverter
//    fun getPhotosFromString(str: String?): List<ImageRoom>? {
//        if (str == null) return null
//        else return Json.decodeFromString<List<ImageRoom>>(str)
//
//    }

}

