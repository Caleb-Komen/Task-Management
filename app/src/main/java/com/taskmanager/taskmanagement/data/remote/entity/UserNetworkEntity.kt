package com.taskmanager.taskmanagement.data.remote.entity

data class UserNetworkEntity(
    val id: String ,
    val name: String,
    val username: String,
    val email: String,
    val photo: String
){
    constructor(): this("", "", "", "", "")
}
