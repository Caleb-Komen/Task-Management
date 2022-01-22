package com.taskmanager.taskmanagement.data.local


sealed class Result<out T>{
    class Success<T>(data: T): Result<T>()
    class Error<T>(message: String): Result<T>()
    object Loading: Result<Nothing>()
}
