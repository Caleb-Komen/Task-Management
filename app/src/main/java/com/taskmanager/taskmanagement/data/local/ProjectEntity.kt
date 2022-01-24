package com.taskmanager.taskmanagement.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taskmanager.taskmanagement.business.model.User
import java.util.*

@Entity(tableName = "projects")
data class ProjectEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "members")
    val members: List<User> = emptyList()
)