package com.utebayKazAlm.countriesapi.util

//Класс для облегчения работы с данными, его ошибками, и его ожиданием.
sealed class ResultOf<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResultOf<T>(data)
    class Error<T>(message: String, data: T? = null) : ResultOf<T>(data, message)
    class Loading<T> : ResultOf<T>()

    //Написал функций которые принимают лямбды, чтобы не использовать when,
    //которая добавляет много вложенности.
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