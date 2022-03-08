package com.taskmanager.taskmanagement.data.local

import com.taskmanager.taskmanagement.data.local.Constants.CACHE_NO_DATA_ERROR
import com.taskmanager.taskmanagement.data.util.Resource

abstract class CacheResponseHandler<T>(
    private val result: CacheResult<T?>
) {
    suspend fun getResult(): Resource<T>?{
        return when (result){
            is CacheResult.Loading -> {
                Resource.loading(null)
            }

            is CacheResult.Error -> {
                Resource.error(result.message)
            }

            is CacheResult.Success -> {
                if (result.data == null){
                    Resource.error(CACHE_NO_DATA_ERROR)
                } else {
                    handleSuccess(result.data)
                }
            }
        }
    }

    abstract suspend fun handleSuccess(data: T): Resource<T>?
}