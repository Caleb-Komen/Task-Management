package com.taskmanager.taskmanagement.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskmanager.taskmanagement.business.model.User

class Converter {
    @TypeConverter
    fun toString(users: List<User>): String{
        return Gson().toJson(users)
    }

    @TypeConverter
    fun fromString(value: String): List<User>{
        val type = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(value, type)
    }
}