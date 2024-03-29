package com.implementing.cozyspace.data.local.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.implementing.cozyspace.model.SubTask
import com.implementing.cozyspace.util.Mood


class DBConverters {

    @TypeConverter
    fun fromSubTasksList(value: List<SubTask>): String {
        val gson = Gson()
        val type = TypeToken.getParameterized(List::class.java, SubTask::class.java).type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSubTasksList(value: String): List<SubTask> {
        val gson = Gson()
        val type = TypeToken.getParameterized(List::class.java, SubTask::class.java).type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toMood(value: Int) = enumValues<Mood>()[value]

    @TypeConverter
    fun fromMood(value: Mood) = value.ordinal
}