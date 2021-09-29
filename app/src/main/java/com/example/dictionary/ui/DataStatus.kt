package com.example.dictionary.ui

sealed class DataStatus <out T>{
    object Loading: DataStatus<Nothing>()
    data class Error(val error: String): DataStatus<Nothing>()
    data class Success<T>(val data: T): DataStatus<T>()
}