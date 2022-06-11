package com.utebayKazAlm.countriesapi.util

sealed class ResultOf<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResultOf<T>(data)
    class Error<T>(message: String, data: T? = null) : ResultOf<T>(data, message)
    class Loading<T> : ResultOf<T>()

    fun onSuccess(callback: (data: T) -> Unit) {
        if (this is Success) callback(data!!)
    }

    fun onError(callback: (message: String, data: T?) -> Unit) {
        if (this is Error) callback(message!!, data)
    }

    fun onLoading(callback: () -> Unit) {
        if (this is Loading) callback()
    }
}