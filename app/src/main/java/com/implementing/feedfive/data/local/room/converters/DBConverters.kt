package com.implementing.feedfive.data.local.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.implementing.feedfive.util.Mood


class DBConverters {

//    @TypeConverter
//    fun fromSubTasksList(value: List<SubTask>): String {
//        val gson = Gson()
//        val type = object : TypeToken<List<SubTask>>() {}.type
//        return gson.toJson(value, type)
//    }

//    @TypeConverter
//    fun toSubTasksList(value: String): List<SubTask> {
//        val gson = Gson()
//        val type = object : TypeToken<List<SubTask>>() {}.type
//        return gson.fromJson(value, type)
//    }

    @TypeConverter
    fun toMood(value: Int) = enumValues<Mood>()[value]

    @TypeConverter
    fun fromMood(value: Mood) = value.ordinal
}