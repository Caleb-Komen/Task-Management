package com.taskmanager.taskmanagement.data.util

data class Resource<out T>(val data: T?, val message: String?){
    companion object {
        fun <T>success(data: T?): Resource<T>{
            return Resource(data, null)
        }

        fun <T>error(message: String?): Resource<T>{
            return Resource(null, message)
        }

        fun <T>loading(data: T?): Resource<T>{
            return Resource(data, null)
        }
    }
}
