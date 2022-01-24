package com.taskmanager.taskmanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taskmanager.taskmanagement.domain.model.User
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