package com.oblessing.app.core.utils

import java.lang.Exception

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Error(val error: Exception) : DataState<Nothing>()
    data class Success<T>(val data: T, val cached: Boolean = false) : DataState<T>()
}