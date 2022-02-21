package com.taskmanager.taskmanagement.data

import android.app.Application
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataFactory @Inject constructor(
    private val application: Application
) {
    fun produceListOfProjects(): List<Project> {
        return Gson()
            .fromJson(
                readJsonFromAssets("projects.json"),
                object : TypeToken<List<Project>>(){}.type
            )
    }

    private fun readJsonFromAssets(filename: String): String?{
        var json: String? = null
        json = try {
            val inputStream = (application.assets as AssetManager).open(filename)
            inputStream.bufferedReader().use {
                it.readText()
            }
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
        return json
    }
}