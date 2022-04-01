package com.fabnie.vendor.repository

sealed class Response<T>(val data: T? = null, val error: String? = null) {
    class Empty<T> : Response<T>()
    class Loading<T> : Response<T>()
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(error: String? = null) : Response<T>(error = error)
}