package com.taskmanager.taskmanagement.data.remote.entity

data class ProjectNetworkEntity(
    val id: String,
    val name: String,
    val taskLists: List<String>,
    val members: List<String>
){
    constructor(): this("", "", emptyList(), emptyList())
}
