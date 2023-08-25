package com.example.jettrivia.data

// Metadata wrapper
data class DataOrException<T, E: Exception>(
    var data: T? = null,
    var isLoading: Boolean = false,
    var exception: E? = null
)