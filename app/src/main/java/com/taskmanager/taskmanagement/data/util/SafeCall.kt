package com.taskmanager.taskmanagement.data.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.taskmanager.taskmanagement.data.local.CacheResult
import com.taskmanager.taskmanagement.data.local.Constants.CACHE_TIMEOUT
import com.taskmanager.taskmanagement.data.local.Constants.CACHE_TIMEOUT_ERROR
import com.taskmanager.taskmanagement.data.local.Constants.CACHE_UNKNOWN_ERROR
import com.taskmanager.taskmanagement.data.remote.Constants.NETWORK_TIMEOUT
import com.taskmanager.taskmanagement.data.remote.Constants.NETWORK_TIMEOUT_ERROR
import com.taskmanager.taskmanagement.data.remote.Constants.NETWORK_UNKNOWN_ERROR
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.IOException

suspend fun <T>safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?>{
    return withContext(dispatcher){
        try {
            withTimeout(CACHE_TIMEOUT){
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable){
            throwable.printStackTrace()
            when(throwable){
                is TimeoutCancellationException -> {
                    CacheResult.Error(CACHE_TIMEOUT_ERROR)
                }
                else -> {
                    CacheResult.Error(CACHE_UNKNOWN_ERROR)
                }
            }
        }
    }
}

suspend fun <T>safeNetworkCall(
    dispatcher: CoroutineDispatcher,
    networkCall: suspend () -> T
): NetworkResult<T>{
    return withContext(dispatcher){
        try {
            withTimeout(NETWORK_TIMEOUT){
                NetworkResult.Success(networkCall.invoke())
            }
        } catch (throwable: Throwable){
            throwable.printStackTrace()
            when(throwable){
                is TimeoutCancellationException -> {
                    val code = 408
                    NetworkResult.GenericError(code, NETWORK_TIMEOUT_ERROR)
                }
                is IOException -> {
                    NetworkResult.NetworkError
                }
                else -> {
                    NetworkResult.GenericError(null, NETWORK_UNKNOWN_ERROR)
                }
            }
        }
    }
}

fun log(message: String?){
    message?.let {
        FirebaseCrashlytics.getInstance().log(it)
    }
}