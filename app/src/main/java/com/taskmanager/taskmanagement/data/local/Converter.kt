package com.taskmanager.taskmanagement.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskmanager.taskmanagement.data.local.entity.UserLocalEntity
import com.taskmanager.taskmanagement.domain.model.User

class Converter {
    @TypeConverter
    fun toString(users: List<UserLocalEntity>): String{
        return Gson().toJson(users)
    }

    @TypeConverter
    fun fromString(value: String): List<UserLocalEntity>{
        val type = object : TypeToken<List<UserLocalEntity>>() {}.type
        return Gson().fromJson(value, type)
    }
}