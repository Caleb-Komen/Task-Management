package com.taskmanager.taskmanagement.data.local

sealed class CacheResult<out T>{
    class Success<T>(data: T): CacheResult<T>()
    class Error<T>(message: String): CacheResult<T>()
    object Loading: CacheResult<Nothing>()
}
