package com.taskmanager.taskmanagement.data.local

sealed class CacheResult<out T>{
    class Success<T>(val data: T?): CacheResult<T>()
    class Error(val message: String?): CacheResult<Nothing>()
    object Loading: CacheResult<Nothing>()
}
