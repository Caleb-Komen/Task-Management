package com.taskmanager.taskmanagement.data.remote

sealed class NetworkResult<out T>{
    class Success<T>(val data: T): NetworkResult<T>()

    class GenericError(val code: Int?, val message: String?): NetworkResult<Nothing>()

    object NetworkError: NetworkResult<Nothing>()
}
